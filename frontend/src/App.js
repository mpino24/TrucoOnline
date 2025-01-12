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
import Game from "./game";                          // <--- your existing or placeholder Game
import TheaterTransition from "./TheaterTransition"; // <--- the new curtain animation

import "./App.css";



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

  function redirectToLogin() {
    if (
      location.pathname !== "/login" &&
      location.pathname !== "/register" &&
      location.pathname !== "/"
    ) {
      navigate("/");
    }
  }

  function updateConnectionTime() {
    fetch("/api/v1/profile/updateConnection", {
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
    });
  }

  useEffect(() => {
    if (jwt) {
      // Validate the token by making a quick API call
      fetch(`/api/v1/jugador?userId=${usuario.id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
      }).then((response) => {
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
      });
    } else {
      redirectToLogin();
    }
  }, []);

  let roles = [];
  if (jwt && validToken) {
    roles = getRolesFromJWT(jwt);
  }

  function getRolesFromJWT(jwt) {
    return jwt_decode(jwt).authorities;
  }

  // Admin routes
  let adminRoutes = <></>;
  roles.forEach((role) => {
    if (role === "ADMIN") {
      adminRoutes = (
        <>
          <Route path="/admin" element={
            <PrivateRoute>
              <AdminHome />
            </PrivateRoute>
          } />
          <Route path="/users" element={
            <PrivateRoute>
              <UserListAdmin />
            </PrivateRoute>
          } />
          <Route path="/users/:username" element={
            <PrivateRoute>
              <UserEditAdmin />
            </PrivateRoute>
          } />
          <Route path="/admin/partidas" element={
            <PrivateRoute>
              <PartidasAdmin />
            </PrivateRoute>
          } />
          <Route path="/admin/estadisticas" element={
            <PrivateRoute>
              <EstadisticasAdmin />
            </PrivateRoute>
          } />
        </>
      );
    }
  });

  // Public routes if token is invalid
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

  // Authenticated user routes if token is valid
  let userRoutes = <></>;
  if (jwt && validToken) {
    userRoutes = (
      <>
        <Route path="/logout" element={<Logout />} />
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/" element={<Login />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/historial" element={<GameHistory />} />

        {/*
          2) Notice how we wrap <Game /> in <TheaterTransition>.
             This ensures the curtains cover the screen, then open to reveal the game.
        */}
        <Route
          path="/partidas"
          element={
            <PrivateRoute>
              <TheaterTransition>
                <Game />
              </TheaterTransition>
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
