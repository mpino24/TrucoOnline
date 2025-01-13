// src/profile/index.js
import React, { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import { Form, Input, Label } from "reactstrap";
import { useNavigate, Link } from "react-router-dom";
import SelectorImagenes from "../util/SelectorImagenes";
import { CSSTransition } from 'react-transition-group'; // <-- NEW IMPORT

// Import the CSS file for the Profile layout

const jwt = tokenService.getLocalAccessToken();

export default function Profile() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [newPassword, setNewPassword] = useState("");
  const [mostrarConfirmarBorrado, setMostrarConfirmarBorrado] = useState(false);
  const navigate = useNavigate();

  const [perfil, setPerfil] = useFetchState(
    {},
    "/api/v1/profile",
    jwt,
    setMessage,
    setVisible
  );

  const [imageModalOpen, setImageModalOpen] = useState(false);
  const [imagenesDisponibles, setImagenesDisponibles] = useFetchState(
    [],
    "/api/v1/fotos/perfiles",
    jwt,
    setMessage,
    setVisible
  );

  const modal = getErrorModal(setVisible, visible, message);

  const handleImageSelect = (imageName) => {
    setPerfil({
      ...perfil,
      photo: `http://localhost:8080/resources/images/perfiles/${imageName}`,
    });
    setImageModalOpen(false);
  };

  function borrarMiCuenta() {
    fetch("/api/v1/profile/borrarMiCuenta", {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
      .then(async (response) => {
        if (response.ok) {
          const data = await response.json();
          setMessage(data.message || "¡Tu cuenta fue borrada con éxito!");
          setVisible(true);
          tokenService.removeUser();
          setTimeout(() => {
            navigate("/");
          }, 3000);
        } else {
          const errorData = await response.json();
          throw new Error(
            errorData.message || "Error al intentar borrar la cuenta."
          );
        }
      })
      .catch((error) => {
        setMessage(error.message);
        setVisible(true);
      });
  }

  function handleSubmit(event) {
    event.preventDefault();
    const updatedPerfil = {
      ...perfil,
      // Only include password if newPassword is not empty
      password: newPassword || undefined,
    };

    fetch("/api/v1/profile/edit", {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedPerfil),
    })
      .then((response) => response.text())
      .then((data) => {
        if (data === "RELOG") {
          tokenService.removeUser();
          navigate("/login");
        } else if (data === "HOME") {
          navigate("/home");
        } else {
          const json = JSON.parse(data);
          if (json.message) {
            let mensaje =
              "Status Code: " + json.statusCode + " -> " + json.message;
            setMessage(mensaje);
            setVisible(true);
          } else {
            navigate("/home");
          }
        }
      })
      .catch((error) => alert(error.message));
  }

  function handleChange(event) {
    const { name, value } = event.target;
    setPerfil({ ...perfil, [name]: value });
  }

  function handlePasswordChange(event) {
    setNewPassword(event.target.value);
  }

  return (
    <div className="profile-page-container">
      {/* Left side: Form and related content */}
      <div className="profile-form-container" style={{marginLeft:"17%"}}>
        {modal}
        {!mostrarConfirmarBorrado && (
          <div className="hero-div">
            <h1 className="loginText" style={{ color: "rgb(255, 223, 65)", fontSize: "50px" }}>
              Editar perfil
            </h1>
            <div className="auth-form-container">
              <Form onSubmit={handleSubmit}>
                {/* Username */}
                <div className="custom-form-input">
                  <Label
                    for="username"
                    className="custom-form-input-label"
                    style={{
                      display: "inline-block",
                      color: "rgb(255, 211, 0)",
                      textShadow: `
                        -1px -1px 0 rgb(169, 59, 0),
                        1px -1px 0 rgb(169, 59, 0),
                        -1px  1px 0 rgb(169, 59, 0),
                        1px  1px 0 rgb(255, 121, 49)
                      `,
                    }}
                  >
                    Nombre de usuario
                  </Label>
                  <Input
                    type="text"
                    name="username"
                    id="username"
                    value={perfil.username || ""}
                    onChange={handleChange}
                    className="custom-input"
                  />
                </div>

                {/* Contraseña */}
                <div className="custom-form-input">
                  <Label
                    for="password"
                    className="custom-form-input-label"
                    style={{
                      display: "inline-block",
                      color: "rgb(255, 211, 0)",
                      textShadow: `
                        -1px -1px 0 rgb(169, 59, 0),
                        1px -1px 0 rgb(169, 59, 0),
                        -1px  1px 0 rgb(169, 59, 0),
                        1px  1px 0 rgb(255, 121, 49)
                      `,
                    }}
                  >
                    Contraseña
                  </Label>
                  <Input
                    type="password"
                    name="password"
                    id="password"
                    value={newPassword}
                    onChange={handlePasswordChange}
                    className="custom-input"
                    placeholder="Deja este campo vacío si no deseas cambiar la contraseña"
                  />
                </div>

                {/* First name */}
                <div className="custom-form-input">
                  <Label
                    for="firstName"
                    className="custom-form-input-label"
                    style={{
                      display: "inline-block",
                      color: "rgb(255, 211, 0)",
                      textShadow: `
                        -1px -1px 0 rgb(169, 59, 0),
                        1px -1px 0 rgb(169, 59, 0),
                        -1px  1px 0 rgb(169, 59, 0),
                        1px  1px 0 rgb(255, 121, 49)
                      `,
                    }}
                  >
                    Nombre
                  </Label>
                  <Input
                    type="text"
                    name="firstName"
                    id="firstName"
                    value={perfil.firstName || ""}
                    onChange={handleChange}
                    className="custom-input"
                  />
                </div>

                {/* Last name */}
                <div className="custom-form-input">
                  <Label
                    for="lastName"
                    className="custom-form-input-label"
                    style={{
                      display: "inline-block",
                      color: "rgb(255, 211, 0)",
                      textShadow: `
                        -1px -1px 0 rgb(169, 59, 0),
                        1px -1px 0 rgb(169, 59, 0),
                        -1px  1px 0 rgb(169, 59, 0),
                        1px  1px 0 rgb(255, 121, 49)
                      `,
                    }}
                  >
                    Apellido
                  </Label>
                  <Input
                    type="text"
                    name="lastName"
                    id="lastName"
                    value={perfil.lastName || ""}
                    onChange={handleChange}
                    className="custom-input"
                  />
                </div>

                {/* Email */}
                <div className="custom-form-input">
                  <Label
                    for="email"
                    className="custom-form-input-label"
                    style={{
                      display: "inline-block",
                      color: "rgb(255, 211, 0)",
                      textShadow: `
                        -1px -1px 0 rgb(169, 59, 0),
                        1px -1px 0 rgb(169, 59, 0),
                        -1px  1px 0 rgb(169, 59, 0),
                        1px  1px 0 rgb(255, 121, 49)
                      `,
                    }}
                  >
                    Email
                  </Label>
                  <Input
                    type="email"
                    name="email"
                    id="email"
                    value={perfil.email || ""}
                    onChange={handleChange}
                    className="custom-input"
                  />
                </div>

                <div className="custom-button-row">
                  <button className="auth-button">Guardar</button>
                  <Link to="/home" className="auth-button" style={{ textDecoration: "none" }}>
                    Volver
                  </Link>
                </div>
              </Form>

              <SelectorImagenes
                imagenes={imagenesDisponibles}
                isOpen={imageModalOpen}
                toggle={() => setImageModalOpen(!imageModalOpen)}
                onSelect={handleImageSelect}
                tipo={"perfil"}
              />
            </div>
          </div>
        )}

        <CSSTransition
                                    in={mostrarConfirmarBorrado}
                                    timeout={300}
                                    classNames="join-modal"
                                    unmountOnExit
                                >
          <div
            className="cuadro-creacion"
            style={{marginLeft:"0%", position:"absolute", }}
          >
            <h3>
              ¿Estás seguro de que querés borrar tu cuenta?
            </h3>
            <img
              src="https://c.tenor.com/gix3ZjueBnMAAAAd/tenor.gif"
              alt="Triste"
              style={{ width: "600px", height: "auto", marginBottom: "20px" }}
            />
            <h4>No se puede revertir del juego ni de nuestros corazones...</h4>
            <button
              onClick={borrarMiCuenta}
             className="dangerButton2"
            >
              Borrar, no quiero seguir disfrutando mi vida al máximo
            </button>
            <button
              onClick={() => setMostrarConfirmarBorrado(false)}
              className="heavenButton"

            >
              Quedarme, jamás dejaría Truco Beasts: Bardo en la Jungla, perdón
            </button>
            </div>
         </CSSTransition>

        {!mostrarConfirmarBorrado && (
          <button
            className="dangerButton2" style={{marginTop:"20px"}}
            onClick={() => setMostrarConfirmarBorrado(true)}
          >
            Borrar mi cuenta
          </button>
        )}
      </div>

      {/* Right side: Image selection preview */}
      <div className="profile-image-selection">
        {perfil.photo && (
          <img
            src={perfil.photo}
            alt="Seleccionada"
            style={{
              width: "150px",
              height: "150px",
              borderRadius: "30%",
              marginBottom: "10px",
              objectFit: "cover"
            }}
          />
        )}
        <button
          type="button"
          onClick={() => setImageModalOpen(true)}
          className="auth-button"
          style={{ marginBottom: "10px" }}
        >
          Seleccionar Imagen
        </button>
      </div>
    </div>
  );
}
