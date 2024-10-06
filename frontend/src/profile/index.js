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
  const [user, setUser] = useFetchState(
    [], "/api/v1/profile", jwt, setMessage, setVisible
  )
  const modal = getErrorModal(setVisible, visible, message);
  const navigate = useNavigate();

  function handleSubmit(event) {
    event.preventDefault();
      fetch(
        "/api/v1/users/" + user.id,
        {
          method: "PUT",
          headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
          },
          body: JSON.stringify(user),
        }
      )
        .then((response) => response.text())
        .then((data) => {
        if(data==="")
          navigate("/");
        else{
          let json = JSON.parse(data);
          if(json.message){
            setMessage(JSON.parse(data).message);
            setVisible(true);
          }else
            navigate("/"); }
          })
      .catch((message) => alert(message));
  }

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setUser({ ...user, [name]: value });
  }

  return(
    <div className="auth-page-container">
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
                required
                name="password"
                id="password"
                value={user.password || ""}
                onChange={handleChange}
                className="custom-input"
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
  );  
}