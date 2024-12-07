import React, { useState } from "react";
import { Alert } from "reactstrap";
import FormGenerator from "../../components/formGenerator/formGenerator";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import { loginFormInputs } from "./form/loginFormInputs";
import logo from '../../static/images/Logo_juego.png'
import { Link } from 'react-router-dom';

export default function Login() {
  const [message, setMessage] = useState(null)
  const loginFormRef = React.createRef();


  async function handleSubmit({ values }) {

    const reqBody = values;
    setMessage(null);
    await fetch("/api/v1/auth/signin", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      body: JSON.stringify(reqBody),
    })
      .then(function (response) {
        if (response.status === 200) return response.json();
        else return Promise.reject("Invalid login attempt");
      })
      .then(function (data) {
        tokenService.setUser(data);
        tokenService.updateLocalAccessToken(data.token);
        window.location.href = "/home";
      })
      .catch((error) => {
        setMessage(error);
      });
  }


  return (
    <div style={{ backgroundImage: 'url(/fondos/fondologin.jpg)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw' }}>
      <div className="auth-page-container">
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}
        <img src={logo} alt='Logo del juego' style={{ height: '130%' }} />
        <div className="auth-form-container">
          <h1 style={{fontSize:49}}>Login</h1>
          <FormGenerator
            ref={loginFormRef}
            inputs={loginFormInputs}
            onSubmit={handleSubmit}
            numberOfColumns={1}
            listenEnterKey
            buttonText="Login"
            buttonClassName="auth-button"
          />
          <div style={{ marginLeft: '35%' }}>
            <p style={{ marginTop: 60, color: '#212529' }}> ¿No tienes cuenta?</p>
            <Link
              to={`/register`}
              style={{ textDecoration: "none" }}
            >
              <button onClick={handleSubmit} className={'auth-button'}>
                Sign up
              </button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}