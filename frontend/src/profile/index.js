import React, { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import getErrorModal from "./../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import { Form, Input, Label } from "reactstrap";
import { useNavigate, Link } from "react-router-dom";

const jwt = tokenService.getLocalAccessToken();

export default function Profile() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [newPassword, setNewPassword] = useState();


  const [perfil, setPerfil] = useFetchState(
    {}, "/api/v1/profile", jwt, setMessage, setVisible
  );


  
  const modal = getErrorModal(setVisible, visible, message);
  const navigate = useNavigate();



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
        <div className="hero-div">
          <h1 className="text-center">Editar perfil</h1>
          <div className="auth-form-container">
            {modal}
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
              {/* Mostrar la foto actual */}
              <div className="custom-form-input">
                <Label for="photo" className="custom-form-input-label">Foto</Label>
                {perfil.photo && (
                  <img
                    src={perfil.photo }
                    alt="Foto del perfil"
                    style={{ width: '100px', height: '100px', borderRadius: '50%' }}
                    onError={(e) => (e.target.style.display = 'none')}
                  />
                )}
                <Label for="photo" className="custom-form-input-label">Foto</Label>
                <Input
                  type="text"
                  name="photo"
                  id="photo"
                  value={perfil.photo }
                  onChange={handleChange}
                  className="custom-input"
                  placeholder="Poné el link de la foto que quieras usar"
                />
              
              </div>
              
              <div className="custom-button-row">
                <button className="auth-button">Guardar</button>
                <Link to={`/`} className="auth-button" style={{ textDecoration: "none" }}>
                  Volver
                </Link>
              </div>
            </Form>
          </div>
        </div>
      </div>
    </div>
  );
}
