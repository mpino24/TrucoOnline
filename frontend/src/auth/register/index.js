import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";

import tokenService from "../../services/token.service";
import FormGenerator from "../../components/formGenerator/formGenerator";
import { registerFormInputs } from "./form/registerForm";
import { useRef, useState } from "react";


export default function Register() {
  let [type, setType] = useState(null);
  let [authority, setAuthority] = useState(null);

  const registerFormRef = useRef();

  function handleButtonClick(event) {
    const target = event.target;
    let value = target.value;
    if (value === "Back") value = null;
    else setAuthority(value);
    setType(value);
  }

  function handleSubmit({ values }) {
    if (!registerFormRef.current.validate()) return;

    const request = values;
    request["authority"] = authority;
    let state = "";

    fetch("/api/v1/auth/signup", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      body: JSON.stringify(request),
    })
      .then(function (response) {
        if (response.status === 200) {
          const loginRequest = {
            username: request.username,
            password: request.password,
          };

          fetch("/api/v1/auth/signin", {
            headers: { "Content-Type": "application/json" },
            method: "POST",
            body: JSON.stringify(loginRequest),
          })
            .then(function (response) {
              if (response.status === 200) {
                state = "200";
                return response.json();
              } else {
                state = "";
                return response.json();
              }
            })
            .then(function (data) {
              if (state !== "200") alert(data.message);
              else {
                tokenService.setUser(data);
                tokenService.updateLocalAccessToken(data.token);
                window.location.href = "/";
              }
            })
            .catch((message) => {
              alert(message);
            });
        }
      })
      .catch((message) => {
        alert(message);
      });
  }

  if (type) {
    return (
      <div 
        style={{ 
          backgroundImage: 'url(/fondos/fondoRegistrarse.jpg)', 
          backgroundSize: 'cover', 
          backgroundRepeat: 'no-repeat', 
          backgroundPosition: 'center', 
          height: '100vh', 
          width: '100vw' 
        }}
      >
        <div 
          className="auth-page-container" 
          style={{ 
            display: 'flex', 
            flexDirection: 'column', 
            justifyContent: 'flex-start', 
            alignItems: 'center', 
            paddingTop: '50px', 
            paddingBottom: '30px' 
          }}
        >
          <h1 className="loginText" style={{
                top: '5%',
                position: 'absolute',
               
              }}
            >Registrarse</h1>

          <div className="auth-form-container" style={{ width: '100%', maxWidth: '400px', top:'5%', position:'fixed'}}>  
            <FormGenerator
              ref={registerFormRef}
              inputs={registerFormInputs}
              onSubmit={handleSubmit}
              numberOfColumns={1}
              listenEnterKey
              buttonText="Save"
              buttonClassName="auth-button"
            />
          </div>
        </div>
      </div>
    );
  } else {
    return (
      <div 
        style={{ 
          backgroundImage: 'url(/fondos/fondoRegistrarse.jpg)', 
          backgroundSize: 'cover', 
          backgroundRepeat: 'no-repeat', 
          backgroundPosition: 'center', 
          height: '100vh', 
          width: '100vw' 
        }}
      >
        <div 
          className="auth-page-container" 
          style={{ 
            display: 'flex', 
            flexDirection: 'column', 
            justifyContent: 'flex-start', 
            alignItems: 'center', 
            paddingTop: '50px', // Espaciado en la parte superior para no solapar
            paddingBottom: '30px'  // Espaciado inferior para el layout general
          }}
        >
          <h1 className="loginText">Registrarse</h1>
          <h2 className="loginText" style={{marginLeft:"58%"}}>
            ¿Cuál será la autoridad de tu usuario?
          </h2>
          <div className="options-row">
            <button
              className="auth-button"
              value="Owner"
              onClick={handleButtonClick}
            >
              Usuario
            </button>
            <button
              className="auth-button"
              value="Vet"
              onClick={handleButtonClick}
            >
              Administrador
            </button>
          </div>
        </div>
      </div>
    );
  }
}
