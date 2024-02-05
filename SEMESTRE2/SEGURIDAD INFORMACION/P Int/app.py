import os
from config import API_KEY,USERDB, PASWDDB

from flask import Flask, jsonify, request
import requests

from pymongo import MongoClient
from bson.objectid import ObjectId
import bcrypt




app = Flask(__name__)
API_KEY = os.getenv('API_KEY', API_KEY)
USERDB = os.getenv('USERDB', USERDB)
PASWDDB = os.getenv('PASWDDB', PASWDDB)

uriDb ='mongodb+srv://'+USERDB+':'+PASWDDB+'@cluster0.nrrtvh8.mongodb.net/?retryWrites=true&w=majority'
client = MongoClient(uriDb)
db = client['user_database']
users_collection = db['users']


def buscar_amenazas(url):
    api_url = f'https://safebrowsing.googleapis.com/v4/threatMatches:find?key={API_KEY}'
    payload = {
        "client": {
            "clientId": "myapp",
            "clientVersion": "1.0.0"
        },
        "threatInfo": {
            "threatTypes": ["MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE"],
            "platformTypes": ["ANY_PLATFORM"],
            "threatEntryTypes": ["URL"],
            "threatEntries": [{"url": url}]
        }
    }

    response = requests.post(api_url, json=payload)
    return response.json()

def verificar_ip(ip):
    response = requests.get(f'https://ipinfo.io/{ip}/json')
    return response.json()

def calcular_sha256(archivo):
    sha256 = hashlib.sha256()
    with open(archivo, "rb") as f:
        for bloque in iter(lambda: f.read(4096), b""):
            sha256.update(bloque)
    return sha256.hexdigest()

def comprobar_archivo_gs(archivo):
    sha256_hash = calcular_sha256(archivo)
    api_url = f'https://safebrowsing.googleapis.com/v4/threatListUpdates:fetch?key={API_KEY}'
    payload = {
        "client": {
            "clientId": "myapp",
            "clientVersion": "1.0.0"
        },
        "threatInfo": {
            "threatTypes": ["MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE"],
            "platformTypes": ["ANY_PLATFORM"],
            "threatEntryTypes": ["EXECUTABLE"],
            "threatEntries": [{"hash": sha256_hash}]
        }
    }

    response = requests.post(api_url, json=payload)
    return response.json()

def hash_password(password):
    return bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())

def verify_password(plain_password, hashed_password):
    return bcrypt.checkpw(plain_password.encode('utf-8'), hashed_password)

@app.route('/buscar_amenazas', methods=['POST'])
def buscar_amenazas_endpoint():
    datos = request.get_json()
    url = datos.get('url')
    
    if not url:
        return jsonify({"error": "Se requiere la URL para buscar amenazas"}), 400

    try:
        amenaza = buscar_amenazas(url)
        return jsonify({"url": url, "amenaza": amenaza})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/verificar_ip', methods=['POST'])
def verificar_ip_endpoint():
    datos = request.get_json()
    ip = datos.get('ip')

    if not ip:
        return jsonify({"error": "Se requiere la dirección IP para verificar"}), 400

    try:
        informacion_ip = verificar_ip(ip)
        return jsonify({"ip": ip, "informacion": informacion_ip})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/comprobar_archivo', methods=['POST'])
def comprobar_archivo_endpoint():
    datos = request.get_json()
    archivo = datos.get('archivo')
    if not archivo:
        return jsonify({"error": "Se requiere el nombre del archivo para comprobar"}), 400

    try:
        resultado_comprobacion = comprobar_archivo_gs(archivo)
        return jsonify({"archivo": archivo, "comprobacion": resultado_comprobacion})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/register', methods=['POST'])
def register_user():
    data = request.json
    username = data.get('username')
    password = data.get('password')
    role = data.get('role')
    email = data.get('email')

    if not username or not password or not role or not email:
        return jsonify({'code':'999', 'error': 'Faltan campos obligatorios'}), 400
##refactorizar
    # Validación de entrada
    if not all([username, password, role, email]) or len(username) > 30 or len(role) > 30 or len(email) > 30 or len(password) > 30:
        return jsonify({'code': '999', 'error': 'Campos no cumplen condiciones'}), 400
#####

    hashed_password = hash_password(password)

    user = {
        'username': username,
        'password': hashed_password,
        'role': role,
        'email': email
    }

    existing_user = users_collection.find_one({'username': username})
    if existing_user:
        return jsonify({'code':'999', 'error': 'El nombre de usuario ya está en uso'}), 400

    result = users_collection.insert_one(user)
    return jsonify({'code':'000', 'message': 'Usuario registrado exitosamente', 'id': str(result.inserted_id)}), 201




# Función para autenticar un usuario
@app.route('/login', methods=['POST'])
def login_user():
    data = request.json
    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        return jsonify({'code':'999', 'error': 'Faltan campos obligatorios'}), 400
##refactorizar
    # Validación de entrada
    if not all([username, password]) or len(username) > 30 or len(password) > 30:
        return jsonify({'code': '999', 'error': 'Campos no cumplen condiciones'}), 400
#####
    user = users_collection.find_one({'username': username})
    if user and verify_password(password, user['password']):
        return jsonify({'code':'000', 'message': 'Inicio de sesión exitoso', 'role': user['role']}), 200
    else:
        return jsonify({'code':'999', 'error': 'Credenciales incorrectas'}), 401



if __name__ == '__main__':
    app.run(debug=True)
