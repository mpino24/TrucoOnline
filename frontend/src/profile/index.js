import React, {useState} from "react";
import tokenService from "../services/token.service";
import getErrorModal from "./../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import { Form, Input, Label } from "reactstrap";
import { useNavigate, Link } from "react-router-dom";

const jwt = tokenService.getLocalAccessToken();

export default function Profile() {

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [newPassword, setNewPassword] = useState("");
  const [user, setUser] = useFetchState(
    [], "/api/v1/profile", jwt, setMessage, setVisible
  )
  const modal = getErrorModal(setVisible, visible, message);
  const navigate = useNavigate();

  function handleSubmit(event) {
    event.preventDefault();
    const updatedUser = {
      ...user,
      password: newPassword ? newPassword : undefined
    };
      fetch(
        "/api/v1/profile/edit",
        {
          method: "PUT",
          headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
          },
          body: JSON.stringify(updatedUser),
        }
      )
        .then((response) => response.text())
        .then((data) => {
        if(data==="Perfil editado con exito") {
          tokenService.removeUser();
          navigate("/login");
        } else if(data === "Nada cambiado"){
            navigate("/");
        } 
        else{
          let json = JSON.parse(data);
          
          if(json.message){
            let mensaje = "Status Code: " + json.statusCode + " -> " + json.message
            setMessage(mensaje);
            setVisible(true);
          }else
            navigate("/"); }
          })
      .catch((error) => alert(error.message));
  }

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setUser({ ...user, [name]: value });
  }

  function handlePasswordChange(event) {
    setNewPassword(event.target.value);
  }

  return(
    <div style={{ backgroundImage: 'url(/fondos/fondologin.jpg)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw' }}>
      <div className="auth-page-container">
        <div className="hero-div">
          <h1 className="text-center">
            Editar perfil
          </h1>
          <div className="auth-form-container">
            {modal}
              <Form onSubmit={handleSubmit}>
                <div className="custom-form-input">
                  <Label for="username" className="custom-form-input-label">
                    Nombre de usuario
                  </Label>
                  <Input
                    type="text"
                    required
                    name="username"
                    id="username"
                    value={user.username || ""}
                    onChange={handleChange}
                    className="custom-input"
                  />
                </div>
                <div className="custom-form-input">
                  <Label for="password" className="custom-form-input-label">
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
                <div className="custom-button-row">
                <button className="auth-button">Guardar</button>
                  <Link
                  to={`/`}
                  className="auth-button"
                  style={{ textDecoration: "none" }}
                  >
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