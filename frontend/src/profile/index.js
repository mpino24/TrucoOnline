import React, { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import getErrorModal from "./../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import { Form, Input, Label } from "reactstrap";
import { useNavigate, Link } from "react-router-dom";
import SelectorImagenes from "../util/SelectorImagenes";
const jwt = tokenService.getLocalAccessToken();

export default function Profile() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [newPassword, setNewPassword] = useState();
  const [mostrarConfirmarBorrado, setMostrarConfirmarBorrado] = useState(false);

  const [perfil, setPerfil] = useFetchState(
    {}, "/api/v1/profile", jwt, setMessage, setVisible
  );

  const [imageModalOpen, setImageModalOpen] = useState(false);

  const [imagenesDisponibles, setImagenesDisponibles] = useFetchState([], "/api/v1/fotos/perfiles", jwt, setMessage, setVisible);

  const handleImageSelect = (imageName) => {
    setPerfil({
      ...perfil,
      photo: `http://localhost:8080/resources/images/perfiles/${imageName}`,
    });
    setImageModalOpen(false);
  };

  const modal = getErrorModal(setVisible, visible, message);
  const navigate = useNavigate();


  function borrarMiCuenta() {
    fetch("/api/v1/profile/borrarMiCuenta", {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      }
    })
      .then(async (response) => {
        if (response.ok) {
          const data = await response.json();
          setMessage(data.message || "¡Tu cuenta fue borrada con éxito!");
          setVisible(true)
          tokenService.removeUser()
          setTimeout(() => {
            navigate("/");
          }, 3000);
        } else {
          const errorData = await response.json();
          throw new Error(errorData.message || "Error al intentar borrar la cuenta.");
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

      password: newPassword ? newPassword : undefined // Solo incluir si hay una nueva contraseña

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
          tokenService.removeUser()
          navigate("/login");
        } else if (data === "HOME") {
          console.log(data)
          navigate("/home");
        } else {
          let json = JSON.parse(data);
          if (json.message) {
            let mensaje = "Status Code: " + json.statusCode + " -> " + json.message;
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
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setPerfil({ ...perfil, [name]: value });
  }


  function handlePasswordChange(event) {
    setNewPassword(event.target.value);
  }

  return (
    <div style={{ backgroundImage: 'url(/fondos/fondologin.jpg)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw' }}>
      <div className="auth-page-container">
        {modal}
        {!mostrarConfirmarBorrado && <div className="hero-div">
          <h1 className="text-center">Editar perfil</h1>
          <div className="auth-form-container">

            <Form onSubmit={handleSubmit}>
              {/* Datos del Usuario */}
              <div className="custom-form-input">
                <Label for="username" className="custom-form-input-label">Nombre de usuario</Label>
                <Input
                  type="text"
                  name="username"
                  id="username"
                  value={perfil.username || ""}
                  onChange={handleChange}
                  className="custom-input"
                />
              </div>
              <div className="custom-form-input">
                <Label for="password" className="custom-form-input-label">Contraseña</Label>
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

              {/* Datos del Jugador */}
              <div className="custom-form-input">
                <Label for="firstName" className="custom-form-input-label">Nombre</Label>
                <Input
                  type="text"
                  name="firstName"
                  id="firstName"
                  value={perfil.firstName || ""}
                  onChange={handleChange}
                  className="custom-input"
                />
              </div>
              <div className="custom-form-input">
                <Label for="lastName" className="custom-form-input-label">Apellido</Label>
                <Input
                  type="text"
                  name="lastName"
                  id="lastName"
                  value={perfil.lastName || ""}
                  onChange={handleChange}
                  className="custom-input"
                />
              </div>
              <div className="custom-form-input">
                <Label for="email" className="custom-form-input-label">Email</Label>
                <Input
                  type="email"
                  name="email"
                  id="email"
                  value={perfil.email || ""}
                  onChange={handleChange}
                  className="custom-input"
                />
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Label className="custom-form-input-label">Foto:  </Label>
                {perfil.photo && (
                  <img
                    src={perfil.photo}
                    alt="Seleccionada"
                    style={{
                      width: "150px",
                      height: "150px",
                      borderRadius: "30%",
                      marginBottom: "10px",
                      objectFit: "cover",
                    }}
                  />
                )}
                <button
                  type="button"
                  onClick={() => setImageModalOpen(true)}
                  className="auth-button"
                  style={{
                    marginBottom: "10px",
                  }}
                >
                  Seleccionar Imagen
                </button>
              </div>

              <div className="custom-button-row">
                <button className="auth-button">Guardar</button>
                <Link to={`/home`} className="auth-button" style={{ textDecoration: "none" }}>
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
        </div>}
        {/* Cuadro de confirmación de borrar mi cuenta */}
        {mostrarConfirmarBorrado && (
          <div
            className="confirmation-dialog"
            style={{
              backgroundColor: "rgba(255, 255, 255, 0.9)",
              padding: "20px",
              borderRadius: "10px",
              boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.3)",
              maxWidth: "1200px",
              margin: "0 auto",
              textAlign: "center"
            }}
          >
            <h3 style={{ color: "black" }}>¿Estás seguro de que querés borrar tu cuenta?</h3>
            <img
              src="https://c.tenor.com/gix3ZjueBnMAAAAd/tenor.gif"
              alt="Triste"
              style={{
                width: "600px",
                height: "auto",
                marginBottom: "20px"
              }}
            />
            <h4>No se puede revertir del juego ni de nuestros corazones...</h4>
            <button
              onClick={borrarMiCuenta}
              style={{
                backgroundColor: "red",
                color: "white",
                padding: "10px",
                marginRight: "10px",
                border: "black",
                borderRadius: "10px",
                cursor: "pointer",
              }}
            >
              Borrar, no quiero seguir disfrutando mi vida al máximo
            </button>
            <button
              onClick={() => setMostrarConfirmarBorrado(false)}
              style={{
                backgroundColor: "green",
                color: "white",
                padding: "10px",
                border: "none",
                borderRadius: "10px",
                cursor: "pointer",
              }}
            >
              Quedarme, jamás dejaría Truco Beasts: Bardo en la Jungla, perdón
            </button>
          </div>
        )}






      </div>
      {/* Borrar mi cuenta */}

      {!mostrarConfirmarBorrado && <button
        className="danger-button"
        onClick={() => setMostrarConfirmarBorrado(true)}
        style={{
          position: "absolute",
          bottom: "10px",
          left: "10px",
          backgroundColor: "red",
          color: "white",
          padding: "10px",
          borderRadius: "5px",
        }}
      >
        Borrar mi cuenta
      </button>}

    </div>


  );
}
