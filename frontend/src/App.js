// App.js
import React, { useEffect, useState } from "react";
import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import jwt_decode from "jwt-decode";
import { ErrorBoundary } from "react-error-boundary";

import AppNavbar from "./AppNavbar";
import Home from "./home";
import PrivateRoute from "./privateRoute";
import Register from "./auth/register";
import Login from "./auth/login";
import Logout from "./auth/logout";
import Profile from "./profile";
import PlanList from "./public/plan";
import tokenService from "./services/token.service";
import AdminHome from "./admin/AdminHome";
import PartidasAdmin from "./admin/partidas/PartidasAdmin";
import EstadisticasAdmin from "./admin/estadisticas/EstadisticasAdmin";
import UserListAdmin from "./admin/users/UserListAdmin";
import UserEditAdmin from "./admin/users/UserEditAdmin";
import GameHistory from "./gamehistory/GameHistory";
import SwaggerDocs from "./public/swagger";

import "./App.css";

// We import TheaterTransition just for /partidas
import TheaterTransition from "./TheaterTransition";
import Game from "./game";

function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  );
}

function App() {
  const TIEMPO_CONEXION_SEGUNDOS = 30000;
  const location = useLocation();
  const navigate = useNavigate();

  // Local state to track if the token is valid
  const [validToken, setValidToken] = useState(false);

  const jwt = tokenService.getLocalAccessToken();
  const usuario = tokenService.getUser();

  // Helper to redirect to login if token is invalid
  function redirectToLogin() {
    if (
      location.pathname !== "/login" &&
      location.pathname !== "/register" &&
      location.pathname !== "/"
    ) {
      navigate("/");
    }
  }

  // Updates user's "last connection" time
  function updateConnectionTime() {
/*    fetch("https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/profile/updateConnection", {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
      },
    }).then((response) => {
      if (!response.ok) {
        alert(response.statusText);
      } else {
        console.log("Tiempo de conexión actualizado");
      }
    });*/
  }

  // Check token validity on mount
  useEffect(() => {
    if (jwt) {
      // Validate token by calling /api/v1/jugador
      fetch(`https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/jugador?userId=${usuario.id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            console.log("Invalid token");
            tokenService.removeUser();
            setValidToken(false);
            redirectToLogin();
          } else {
            setValidToken(true);
            updateConnectionTime();

            // Periodically update connection time
            const timer = setInterval(() => {
              updateConnectionTime();
            }, TIEMPO_CONEXION_SEGUNDOS - 1);
            return () => clearInterval(timer);
          }
        })
        .catch((err) => {
          console.error("Token validation error:", err);
          setValidToken(false);
          redirectToLogin();
        });
    } else {
      // No token => must log in
      redirectToLogin();
    }
  }, []);

  // Read roles from JWT if valid
  let roles = [];
  if (jwt && validToken) {
    roles = getRolesFromJWT(jwt);
  }

  function getRolesFromJWT(token) {
    return jwt_decode(token).authorities;
  }

  // Admin routes
  let adminRoutes = <></>;
  roles.forEach((role) => {
    if (role === "ADMIN") {
      adminRoutes = (
        <>
          <Route
            path="/admin"
            element={
              <PrivateRoute>
                <AdminHome />
              </PrivateRoute>
            }
          />
          <Route
            path="/users"
            element={
              <PrivateRoute>
                <UserListAdmin />
              </PrivateRoute>
            }
          />
          <Route
            path="/users/:username"
            element={
              <PrivateRoute>
                <UserEditAdmin />
              </PrivateRoute>
            }
          />
          <Route
            path="/admin/partidas"
            element={
              <PrivateRoute>
                <PartidasAdmin />
              </PrivateRoute>
            }
          />
          <Route
            path="/admin/estadisticas"
            element={
              <PrivateRoute>
                <EstadisticasAdmin />
              </PrivateRoute>
            }
          />
        </>
      );
    }
  });

  // Public routes if token invalid
  let publicRoutes = <></>;
  if (!jwt || !validToken) {
    publicRoutes = (
      <>
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
      </>
    );
  }

  // Authenticated routes if token valid
  let userRoutes = <></>;
  if (jwt && validToken) {
    userRoutes = (
      <>
        <Route path="/logout" element={<Logout />} />
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/" element={<Login />} />
        <Route path="/profile" element={
          
          
          <PrivateRoute>

          <Profile />
          </PrivateRoute>
        } />
        <Route path="/historial" element={<GameHistory />} />

        {/*
          Curtains ONLY for /partidas:
          Wrap <Game /> in <TheaterTransition> inside the PrivateRoute.
        */}
        <Route
          path="/partidas"
          element={
            <PrivateRoute>
                <Game />
            </PrivateRoute>
          }
        />
      </>
    );
  }

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback}>
        <AppNavbar />
        <Routes location={location}>
          <Route path="/plans" element={<PlanList />} />
          <Route path="/docs" element={<SwaggerDocs />} />
          {publicRoutes}
          {userRoutes}
          {adminRoutes}
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;
