import os
from config import API_KEY,USERDB, PASWDDB, API_KEY_VIRUSTOTAL, URLDB
from flask import Flask, jsonify, request, send_file
from flask_cors import CORS
import requests
from pymongo import MongoClient
from bson.objectid import ObjectId
import bcrypt
from utils import hash_password, verify_password, buscar_amenazas,  calcular_sha256, comprobar_archivo_gs, allowed_file
import logging
import csv
import json
from io import StringIO, BytesIO

from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import padding
from base64 import b64encode, b64decode



app = Flask(__name__)
CORS(app) 





def encrypt_AES256(key, data):
    assert len(key) == 32, "La clave AES debe tener 32 bytes"
    iv = os.urandom(16)
    cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
    encryptor = cipher.encryptor()
    padder = padding.PKCS7(128).padder()
    padded_data = padder.update(data) + padder.finalize()
    ciphertext = encryptor.update(padded_data) + encryptor.finalize()
    return b64encode(iv + ciphertext)

def decrypt_AES256(encrypted_data):
    key = b':\x9b\xb0W\x0b}\x06]\xd0gxX$2\xc8\xed\x0b\x9fN\xcb3L;8\xd3%:5\x94\x10\xcf\xa5'
    assert len(key) == 32, "La clave AES debe tener 32 bytes"
    encrypted_data = b64decode(encrypted_data)
    iv = encrypted_data[:16]
    ciphertext = encrypted_data[16:]
    cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
    decryptor = cipher.decryptor()
    decrypted_data = decryptor.update(ciphertext) + decryptor.finalize()
    unpadder = padding.PKCS7(128).unpadder()
    unpadded_data = unpadder.update(decrypted_data) + unpadder.finalize()
    return unpadded_data
    
API_KEY = os.getenv('API_KEY', API_KEY)
API_KEY_VIRUSTOTAL = os.getenv('API_KEY_VIRUSTOTAL', API_KEY_VIRUSTOTAL)
USERDB = os.getenv('USERDB', USERDB)
PASWDDB = os.getenv('PASWDDB', PASWDDB)
URLDB = os.getenv('URLDB', URLDB)

API_KEY = decrypt_AES256(API_KEY)
API_KEY_VIRUSTOTAL = decrypt_AES256(API_KEY_VIRUSTOTAL)
USERDB = decrypt_AES256(USERDB)
PASWDDB = decrypt_AES256(PASWDDB)
URLDB = decrypt_AES256(URLDB)

API_KEY = API_KEY.decode('utf-8')
API_KEY_VIRUSTOTAL = API_KEY_VIRUSTOTAL.decode('utf-8')
USERDB = USERDB.decode('utf-8')
PASWDDB = PASWDDB.decode('utf-8')
URLDB = URLDB.decode('utf-8')

uriDb ='mongodb+srv://'+USERDB+':'+PASWDDB+'@'+URLDB+'/?retryWrites=true&w=majority'
client = MongoClient(uriDb)
db = client['user_database']
users_collection = db['users']


logging.basicConfig(filename='app.log', encoding='utf-8', level=logging.DEBUG)



@app.route('/gsb/listas_navegacion')
def get_threat_lists():
    url = f"https://safebrowsing.googleapis.com/v4/threatLists?key={API_KEY}"
    response = requests.get(url)
    if response.status_code == 200:
        return jsonify(response.json())
    else:
        return jsonify({"error": "Error al obtener la lista de amenazas"}), response.status_code

### SI SE QUEDA
@app.route('/gsb/buscar_amenazas', methods=['POST'])
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
### si se queda
@app.route('/ipinfo/verificar_ip', methods=['POST'])
def verificar_ip_endpoint():
    datos = request.get_json()
    ip = datos.get('ip')

    if not ip:
        logging.info('No hay ip')
        return jsonify({"error": "Se requiere la dirección IP para verificar"}), 400
    try:
        informacion_ip = requests.get(f'https://ipinfo.io/{ip}/json').json()
        logging.info('informacion_ip: %s', informacion_ip)
        return jsonify({"ip": ip, "informacion": informacion_ip})
    except Exception as e:
        logging.info('EXCEPCION : %s', e)
        return jsonify({"error": str(e)}), 500

#no se queda
@app.route('/comprobar_archivo', methods=['POST'])
def comprobar_archivo_endpoint():
    # datos = request.get_json()
    # archivo = datos.get('archivo')

    file = request.files['archivo']
    if not file:
        logging.info('No hay archivo')
        return jsonify({"error": "Se requiere el nombre del archivo para comprobar"}), 400

    try:
        resultado_comprobacion = comprobar_archivo_gs(file, API_KEY)
        logging.info('resultado_comprobacion: %s', resultado_comprobacion)

        return jsonify({"archivo": "archivo", "comprobacion": resultado_comprobacion})
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
    if not all([username, password, role, email]) or len(username) > 30 or len(role) > 30 or len(email) > 30 or len(password) > 30:
        logging.info('no cumple condiciones' )
        
        return jsonify({'code': '999', 'error': 'Campos no cumplen condiciones'}), 400
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

from flask import Response, json

def json_response(data, status_code):
    """
    Serialize the data to JSON and construct a Flask Response with the specified status code.
    """
    return Response(
        response=json.dumps(data, default=str),  # Use default=str to handle non-serializable types
        status=status_code,
        mimetype='application/json'
    )

@app.route('/get_users', methods=['GET'])
def get_users():
    users = users_collection.find({}, {'_id': False})  # Exclude _id field from the response
    user_list = list(users)
    if not user_list:
        return json_response({'code': '404', 'message': 'No se encontraron usuarios'}, 404)
    for user in user_list:
        user.pop('password', None)
    return json_response({'code': '200', 'users': user_list}, 200)

# Función para autenticar un usuario
@app.route('/login', methods=['POST'])
def login_user():
    data = request.json
    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        logging.info('no hay datos entrada' )

        return jsonify({'code':'999', 'error': 'Faltan campos obligatorios'}), 400
    if not all([username, password]) or len(username) > 30 or len(password) > 30:
        logging.info('datos entrada no cumple condiciones' )
        
        return jsonify({'code': '999', 'error': 'Campos no cumplen condiciones'}), 400
    user = users_collection.find_one({'username': username})
    if user and verify_password(password, user['password']):
        logging.info('inicio exitoso' )

        return jsonify({'code':'000', 'message': 'Inicio de sesión exitoso', 'role': user['role']}), 200
    else:
        logging.info('credenciales incorrectas' )
        
        return jsonify({'code':'999', 'error': 'Credenciales incorrectas'}), 401

#se queda
@app.route('/virus_total/consultar_ip', methods=['POST'])
def consultar_ip_route():
    ip = request.json.get('ip')
    if ip:
        resultado = consultar_ip(API_KEY_VIRUSTOTAL, ip)
        if resultado:
            return jsonify(resultado)
        else:
            return jsonify({"error": "No se pudo consultar la IP"}), 500
    else:
        return jsonify({"error": "Se requiere una dirección IP en el cuerpo de la solicitud"}), 400


#se queda
@app.route('/virus_total/consultar_archivo_ips', methods=['POST'])
def consultar_ips_route():
    if 'file' not in request.files:
        return jsonify({"error": "No se proporcionó un archivo"}), 400
    
    file = request.files['file']
    
    if file.filename == '':
        return jsonify({"error": "Nombre de archivo vacío"}), 400

    if not allowed_file(file.filename):
        return jsonify({"error": "Tipo de archivo no permitido. Solo se permiten archivos .txt"}), 400
    ips = []
    for line in file:
        ip = line.decode().strip()
        ips.append(ip)

    resultados = []
    for ip in ips:
        resultado = consultar_ip(API_KEY_VIRUSTOTAL, ip)
        if resultado:
            propietario = resultado['data']['attributes'].get('as_owner', 'No disponible')
            categoria = resultado['data']['attributes']['last_analysis_stats']
            resultado_fila = [ip, propietario, categoria.get('malicious', 'No disponible'), categoria.get('suspicious', 'No disponible')]
            resultados.append(resultado_fila)

    output = StringIO()
    archivo_csv = csv.writer(output)
    cabeceras = ["IP", "Propietario", "Malicious", "Suspicious"]
    archivo_csv.writerow(cabeceras)
    archivo_csv.writerows(resultados)
    output.seek(0)

    output_bytes = BytesIO()
    output_bytes.write(output.getvalue().encode())
    output_bytes.seek(0)

    return send_file(output_bytes, mimetype='text/csv', as_attachment=True, download_name='resultados.csv'), 200

def consultar_ip(api_key, ip):
    url = f"https://www.virustotal.com/api/v3/ip_addresses/{ip}"
    headers = {
        "x-apikey": api_key
    }
    response = requests.get(url, headers=headers)
    print(response.json())

    if response.status_code == 200:
        resultado = response.json()
        return resultado
    else:
        print(f"Error al consultar la IP {ip}. Código de estado: {response.status_code}")
        return None

def consulta_ip_file():
    archivo_ips = "ips.txt"
    resultados = []

    with open(archivo_ips, "r") as file:
        for linea in file:
            ip = linea.strip()
            resultado = consultar_ip(API_KEY_VIRUSTOTAL, ip)
            if resultado:
                print(resultado)
                archivo_json = "resultados.json"

                with open(archivo_json, "w") as file:
                    json.dump(resultado, file, indent=4)

                propietario = resultado['data']['attributes']['last_analysis_results']
                categoria = resultado['data']['attributes']['last_analysis_stats']
                resultado_fila = [ip, propietario, categoria['malicious'], categoria['suspicious']]
                resultados.append(resultado_fila)
                
    archivo_csv = "resultados.csv"
    cabeceras = ["IP", "Propietario", "Malicious", "Suspicious"]

    with open(archivo_csv, "w", newline="") as file:
        writer = csv.writer(file)
        writer.writerow(cabeceras)
        writer.writerows(resultados)

    print("Los resultados se han guardado en el archivo 'resultados.csv'.")

###############################

if __name__ == '__main__':
    app.run(debug=True)
