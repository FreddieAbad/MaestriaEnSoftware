import os
from config import API_KEY,USERDB, PASWDDB
from flask import Flask, jsonify, request
import requests
from pymongo import MongoClient
from bson.objectid import ObjectId
import bcrypt
from utils import hash_password, verify_password, buscar_amenazas, verificar_ip, calcular_sha256, comprobar_archivo_gs
import logging

app = Flask(__name__)
API_KEY = os.getenv('API_KEY', API_KEY)
USERDB = os.getenv('USERDB', USERDB)
PASWDDB = os.getenv('PASWDDB', PASWDDB)

uriDb ='mongodb+srv://'+USERDB+':'+PASWDDB+'@cluster0.nrrtvh8.mongodb.net/?retryWrites=true&w=majority'
client = MongoClient(uriDb)
db = client['user_database']
users_collection = db['users']

# logging.basicConfig(
#     filename='app.log',
#     level=logging.DEBUG,
#     format='%(asctime)s - %(levelname)s - %(message)s',
#     datefmt='%Y-%m-%d %H:%M:%S.%f'
# )


# Configuración del sistema de logs
logging.basicConfig(filename='app.log', level=logging.DEBUG)

@app.route('/buscar_amenazas', methods=['POST'])
def buscar_amenazas_endpoint():
    datos = request.get_json()
    url = datos.get('url')
    logging.info('url: %s', url)

    if not url:
        logging.info('No hay url')
        return jsonify({"error": "Se requiere la URL para buscar amenazas"}), 400
    try:
        amenaza = buscar_amenazas(url, API_KEY)
        logging.info('Amenaza encontrada: %s', amenaza)

        return jsonify({"url": url, "amenaza": amenaza})
    except Exception as e:
        logging.info('EXCEPCION : %s', e)

        return jsonify({"error": str(e)}), 500

@app.route('/verificar_ip', methods=['POST'])
def verificar_ip_endpoint():
    datos = request.get_json()
    ip = datos.get('ip')

    if not ip:
        logging.info('No hay ip')
        return jsonify({"error": "Se requiere la dirección IP para verificar"}), 400

    try:
        informacion_ip = verificar_ip(ip)
        logging.info('informacion_ip: %s', informacion_ip)
        return jsonify({"ip": ip, "informacion": informacion_ip})
    except Exception as e:
        logging.info('EXCEPCION : %s', e)
        return jsonify({"error": str(e)}), 500

@app.route('/comprobar_archivo', methods=['POST'])
def comprobar_archivo_endpoint():
    datos = request.get_json()
    archivo = datos.get('archivo')
    if not archivo:
        logging.info('No hay archivo')
        return jsonify({"error": "Se requiere el nombre del archivo para comprobar"}), 400

    try:
        resultado_comprobacion = comprobar_archivo_gs(archivo, API_KEY)
        logging.info('resultado_comprobacion: %s', resultado_comprobacion)

        return jsonify({"archivo": archivo, "comprobacion": resultado_comprobacion})
    except Exception as e:
        logging.info('exce: %s', e)

        return jsonify({"error": str(e)}), 500

@app.route('/register', methods=['POST'])
def register_user():
    data = request.json
    username = data.get('username')
    password = data.get('password')
    role = data.get('role')
    email = data.get('email')

    if not username or not password or not role or not email:
        logging.info('no hay parametros de entrada')

        return jsonify({'code':'999', 'error': 'Faltan campos obligatorios'}), 400
##refactorizar
    if not all([username, password, role, email]) or len(username) > 30 or len(role) > 30 or len(email) > 30 or len(password) > 30:
        logging.info('no cumple condiciones' )
        
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
        logging.info('usuario ya existe' )

        return jsonify({'code':'999', 'error': 'El nombre de usuario ya está en uso'}), 400

    result = users_collection.insert_one(user)
    logging.info('resultado %s', result )

    return jsonify({'code':'000', 'message': 'Usuario registrado exitosamente', 'id': str(result.inserted_id)}), 201




# Función para autenticar un usuario
@app.route('/login', methods=['POST'])
def login_user():
    data = request.json
    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        logging.info('no hay datos entrada' )

        return jsonify({'code':'999', 'error': 'Faltan campos obligatorios'}), 400
##refactorizar
    if not all([username, password]) or len(username) > 30 or len(password) > 30:
        logging.info('datos entrada no cumple condiciones' )
        
        return jsonify({'code': '999', 'error': 'Campos no cumplen condiciones'}), 400
#####
    user = users_collection.find_one({'username': username})
    if user and verify_password(password, user['password']):
        logging.info('inicio exitoso' )

        return jsonify({'code':'000', 'message': 'Inicio de sesión exitoso', 'role': user['role']}), 200
    else:
        logging.info('credenciales incorrectas' )
        
        return jsonify({'code':'999', 'error': 'Credenciales incorrectas'}), 401



if __name__ == '__main__':
    app.run(debug=True)
