import React from "react";
import { Link } from "react-router-dom";
import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";
import tokenService from "../../services/token.service";

const Logout = () => {

  const jwt = tokenService.getLocalAccessToken();


  function sendLogoutRequest() {
    if (jwt || typeof jwt === "undefined") {
      tokenService.removeUser();
      window.localStorage.removeItem("jwt");
      window.location.href = "/";
    }
  }

  return (
    <div style={{paddingTop:"1px",backgroundImage: 'url(https://www.pukaarnews.com/wp-content/uploads/2016/03/Bonobo-14.jpg)', backgroundSize: 'cover',height: '100vh', width: '100vw' }}>
    <div className="auth-page-container">
      <div className="auth-form-container">
        <h2 className="loginText" style={{width:"120%"}}>
          ¿Seguro que querés cerrar sesión?
        </h2>
        <div className="options-row">
          <Link className="heavenButton" to="/home" style={{ textDecoration: "none" }}>
            No
          </Link>
          <button className="dangerButton2" onClick={() => sendLogoutRequest()}>
            Sí
          </button>
        </div>
      </div>
    </div>
    </div>
  );
};

export default Logout;
