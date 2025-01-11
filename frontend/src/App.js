import React, { useEffect } from "react";
import { Route, Routes, useLocation } from "react-router-dom";
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
import Game from "./game"
import SwaggerDocs from "./public/swagger";
import './App.css';
import { useNavigate } from "react-router-dom";

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
  const jwt = tokenService.getLocalAccessToken();
  const [validToken, setValidToken] = React.useState(false);
  let roles = [];
  const navigate = useNavigate();
  const usuario = tokenService.getUser();

  function redirectToLogin() {
    if (location.pathname !== "/login" && location.pathname !== "/register" && location.pathname !== "/") {
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
        if (response.ok) {
          console.log("Tiempo de conexión actualizado");
        }
      }
      );
  }

  useEffect(() => {
    if (jwt) {
      fetch(`/api/v1/jugador?userId=` + usuario.id, { //Esta llamada es simplemente para comprobar si el jwt es válido
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
          
          const timer = setInterval(() => {
            updateConnectionTime();
          },TIEMPO_CONEXION_SEGUNDOS-1);
          return () => clearInterval(timer);

        }
      }
      )
    } else {
      redirectToLogin();
    }
  }, []);



  if (jwt && validToken) {
    roles = getRolesFromJWT(jwt);
  }

  function getRolesFromJWT(jwt) {
    return jwt_decode(jwt).authorities;
  }

  let adminRoutes = <></>;
  let userRoutes = <></>;
  let publicRoutes = <></>;

  roles.forEach((role) => {
    if (role === "ADMIN") {
      adminRoutes = (
        <>
          <Route path="/admin" element={<PrivateRoute><AdminHome /></PrivateRoute>} />
          <Route path="/users" element={<PrivateRoute><UserListAdmin /></PrivateRoute>} />
          <Route path="/users/:username" element={<PrivateRoute><UserEditAdmin /></PrivateRoute>} />
          <Route path="/admin/partidas" element={<PrivateRoute><PartidasAdmin /></PrivateRoute>} />
          <Route path="/admin/estadisticas" element={<PrivateRoute><EstadisticasAdmin /></PrivateRoute>} />
        </>
      );
    }
  });

  if (!jwt || !validToken) {
    publicRoutes = (
      <>
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />

      </>
    );
  } else {
    userRoutes = (
      <>
        <Route path="/logout" element={<Logout />} />
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/" element={<Login />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/partidas" element={<Game />} />
        <Route path="/historial" element={<GameHistory />} />
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
