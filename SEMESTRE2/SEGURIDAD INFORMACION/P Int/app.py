import os
from config import API_KEY

from flask import Flask, jsonify, request
import requests

app = Flask(__name__)
API_KEY = os.getenv('API_KEY', API_KEY)

#API_KEY = 'AIzaSyAE7j8IWquf72ha9XGDs06bqDDyOoxuLe8'

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
    # Utilizando la API ipinfo.io para obtener información sobre la IP
    response = requests.get(f'https://ipinfo.io/{ip}/json')
    return response.json()

# Función para calcular el hash SHA256 del archivo
def calcular_sha256(archivo):
    sha256 = hashlib.sha256()
    with open(archivo, "rb") as f:
        # Lee el archivo en bloques para manejar archivos grandes
        for bloque in iter(lambda: f.read(4096), b""):
            sha256.update(bloque)
    return sha256.hexdigest()

# Función para comprobar archivo con Google Safe Browsing
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

# Ruta para comprobar archivo
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

if __name__ == '__main__':
    app.run(debug=True)
