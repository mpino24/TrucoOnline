import React from "react";
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
import PartidasTerminadasAdmin from "./admin/partidas/PartidasTerminadasAdmin";
import UserListAdmin from "./admin/users/UserListAdmin";
import UserEditAdmin from "./admin/users/UserEditAdmin";
import Game from "./game"
import SwaggerDocs from "./public/swagger";
import './App.css'; // Make sure to import your CSS file with the transition styles

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
  const location = useLocation();
  const jwt = tokenService.getLocalAccessToken();
  let roles = [];

  function connectUser() {
    fetch(
      "/api/v1/profile/connect",
      {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      }
    )
      .then((response) => {
        if (!response.ok) {
          alert("There was an error connecting the user");
        }
      })
      .catch((message) => alert(message));
  }

  if (jwt) {
    connectUser();
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
          <Route path="/admin/partidas/terminadas" element={<PrivateRoute><PartidasTerminadasAdmin /></PrivateRoute>} />
        </>
      );
    }
  });

  if (!jwt) {
    publicRoutes = (
      <>
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Login />} />
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

      </>
    );
  }

  function disconnectUser() {
    if (jwt) {
      console.log("Disconnecting user");
      fetch("/api/v1/profile/disconnect", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
      }).then((response) => {
        if (!response.ok) {
          alert(response.statusText);
          alert("There was an error closing the session");
        }
      }
      );
    }
  }

  window.addEventListener('beforeunload', function (event) {
    disconnectUser();
  });


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
