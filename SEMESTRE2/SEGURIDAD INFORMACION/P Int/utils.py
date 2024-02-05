import bcrypt
import requests
import hashlib

def buscar_amenazas(url, API_KEY):
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

def comprobar_archivo_gs(archivo,API_KEY):
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
