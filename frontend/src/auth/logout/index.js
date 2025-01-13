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
    <div className="auth-page-container">
      <div className="auth-form-container">
        <h2 className="loginText" style={{color: "black"}}>
          ¿Seguro que querés cerrar sesión?
        </h2>
        <div className="options-row">
          <Link className="auth-button" to="/home" style={{ textDecoration: "none" }}>
            No
          </Link>
          <button className="auth-button" onClick={() => sendLogoutRequest()}>
            Yes
          </button>
        </div>
      </div>
    </div>
  );
};

export default Logout;
