from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import padding
from base64 import b64encode, b64decode
import os
def encrypt_AES256(key, data):
    assert len(key) == 32, "La clave AES debe tener 32 bytes"
    iv = os.urandom(16)
    cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
    encryptor = cipher.encryptor()
    padder = padding.PKCS7(128).padder()
    padded_data = padder.update(data) + padder.finalize()
    ciphertext = encryptor.update(padded_data) + encryptor.finalize()
    return b64encode(iv + ciphertext)

def decrypt_AES256(key, encrypted_data):
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

if __name__ == "__main__":
    #random_bytes = os.urandom(32)
    #print(random_bytes)

    key = b':\x9b\xb0W\x0b}\x06]\xd0gxX$2\xc8\xed\x0b\x9fN\xcb3L;8\xd3%:5\x94\x10\xcf\xa5'
    plaintext = b'00869999841fe2e76706de24f3e697773d08e393dbfa92c290583558a0c3011c'

    encrypted_data = encrypt_AES256(key, plaintext)
    print("Texto cifrado:", encrypted_data)

    decrypted_data = decrypt_AES256(key, encrypted_data)
    print("Texto descifrado:", decrypted_data.decode())



