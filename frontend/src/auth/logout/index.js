import React from "react";
import { Link } from "react-router-dom";
import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";
import tokenService from "../../services/token.service";

const Logout = () => {

  const jwt = tokenService.getLocalAccessToken();


  function sendLogoutRequest() {
    if (jwt || typeof jwt === "undefined") {

      fetch("/api/v1/profile/disconnect", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
      }).then((response) => {
        if (response.ok) {
          tokenService.removeUser();
          window.localStorage.removeItem("jwt");
          window.location.href = "/";
        } else {
          alert("There was an error logging out");
        }
      }
      );
    } else {
      alert("There is no user logged in");
    }
  }

  return (
    <div className="auth-page-container">
      <div className="auth-form-container">
        <h2 className="text-center text-md">
          Are you sure you want to log out?
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
