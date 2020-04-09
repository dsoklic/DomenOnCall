import psutil
from flask import Flask

app = Flask(__name__)

@app.route('/')
def check_zoom():
    return str(is_zoom_call())

def is_zoom_call():
    for p in psutil.process_iter():
        try:
            if p.name() == "CptHost.exe":
                return True
        except psutil.AccessDenied:
            pass

    return False

if __name__ == '__main__':
    app.run(host='0.0.0.0', port='5000')
