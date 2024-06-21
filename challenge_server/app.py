from flask import Flask, request, jsonify
from datetime import datetime, timedelta
import json
from Crypto.Cipher import ARC4
import random, string

app = Flask(__name__)

# Generate a random 3-digit key when the server starts up
generated_key = ''.join(random.choice(string.digits) for _ in range(3))

# Dummy user credentials
user_credentials = {
    'username': 'user',
    'password': 'user123'
}

def rc4_encrypt(plain_text, key='secret_key'):
    cipher = ARC4.new(key.encode())
    return cipher.encrypt(plain_text.encode()).hex()

def rc4_decrypt(cipher_text, key='secret_key'):
    cipher = ARC4.new(key.encode())
    return cipher.decrypt(bytes.fromhex(cipher_text)).decode()


# Function to generate encrypted token
def generate_token(username, is_admin=False):
    payload = {
        'username': username,
        'exp': (datetime.utcnow() + timedelta(hours=1)).isoformat()  # Token expiration time
    }
    if is_admin:
        payload['isAdmin'] = True
    token = json.dumps(payload)  # Convert payload to JSON string
    encrypted_token = rc4_encrypt(token, generated_key)
    return encrypted_token

# Function to verify encryted token
def verify_token(encrypted_token):
    try:
        decrypted_token = rc4_decrypt(encrypted_token, generated_key)
        payload = json.loads(decrypted_token)  # Convert JSON string to Python dictionary
        return payload
    except json.JSONDecodeError:
        return jsonify({'message': 'Invalid token. Please log in again.'}), 401 
    except TypeError:
        return jsonify({'message': 'Invalid token. Please log in again.'}), 401 
    except UnicodeDecodeError:
        return jsonify({'message': 'Invalid token. Please log in again.'}), 401 

# Endpoint to generate a token
@app.route('/token', methods=['POST'])
def generate_jwt_token():
    data = request.json
    if data['username'] == user_credentials['username'] and data['password'] == user_credentials['password']:
        token = generate_token(data['username'],is_admin=False)
        return jsonify({'token': token}), 200
    else:
        return jsonify({'message': 'Invalid username or password'}), 401

# Endpoint to perform admin actions
@app.route('/admin', methods=['POST'])
def admin_actions():
    token = request.headers.get('Authorization')
    if token:
        token = token.split(" ")[1]  # Remove 'Bearer ' prefix
        payload = verify_token(token)
        if isinstance(payload, dict):
            is_admin = payload.get('isAdmin', False)
            if is_admin:
                # Perform admin actions here
                return jsonify({'message': 'Admin actions performed successfully'}), 200
            else:
                return jsonify({'message': 'Unauthorized access'}), 401
        else:
            return jsonify({'message': 'Invalid token. Please log in again.'}), 401
    else:
        return jsonify({'message': 'Token is missing'}), 401


if __name__ == '__main__':
    app.run(debug=False)
