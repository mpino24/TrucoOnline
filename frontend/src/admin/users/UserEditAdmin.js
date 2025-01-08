import { useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchData from "../../util/useFetchData";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function UserEditAdmin() {
  const emptyUser = {
    id: null,
    username: "",
    password: "",
    authority: null,
    isConnected: false
  };
  const emptyPlayer = {
    firstName: "",
    lastName: "",
    email: "",
    photo: ""
  }
  const [newPassword, setNewPassword] = useState();
  const id = getIdFromUrl(2);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [player, setPlayer] = useFetchState(
    emptyPlayer,
    `/api/v1/jugador/edit/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  
  const [user, setUser] = useFetchState(
    emptyUser,
    `/api/v1/users/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  const auths = useFetchData(`/api/v1/users/authorities`, jwt);

  function handleUserChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    if (name === "authority") {
      const auth = auths.find((a) => a.id === Number(value));
      setUser({ ...user, authority: auth });
    } else setUser({ ...user, [name]: value });
  }

  function handlePlayerChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setPlayer({...player, [name]: value})
  }

  function handleSubmit(event) {
    event.preventDefault();

    const request = {
      username: user.username,
      password: user.password,
      authority: user.authority?.id,
      firstName: player.firstName,
      lastName: player.lastName,
      email: player.email,
      photo: player.photo,
    };

    user.password = newPassword ? newPassword : undefined;
  
    const endpoint = user.id ? `/api/v1/users/${user.id}` : "/api/v1/auth/signup";
    const method = user.id ? "PUT" : "POST";
    const body = user.id ? JSON.stringify(user) : JSON.stringify(request)
  
    fetch(endpoint, {
      method: method,
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: body,
    })
      .then((response) => response.json())
      .then((json) => {
        if (json.message) {
          setMessage(json.message);
          setVisible(true);
        } else {
          if (user.id) {
            return fetch(`/api/v1/jugador/edit/${user.id}`, {
              method: "PUT",
              headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
              },
              body: JSON.stringify({ ...player, userId: user.id }),
            });
          } else {
            window.location.href = "/users";
          }
        }
      })
      .then((response) => response?.json())
      .then((json) => {
        if (json?.message) {
          setMessage(json.message);
          setVisible(true);
        } else {
          window.location.href = "/users";
        }
      })
      .catch((error) => alert(error.message));
  }

  const modal = getErrorModal(setVisible, visible, message);
  const authOptions = auths.map((auth) => (
    <option key={auth.id} value={auth.id}>
      {auth.authority}
    </option>
  ));

  function handlePasswordChange(event) {
    const newPass = event.target.value;
    setNewPassword(newPass);
    setUser({ ...user, password: newPass });
  }

  return (
    <div style={{ 
      backgroundImage: 'url(/fondos/fondo_admin.png)', 
      backgroundSize: 'cover', 
      backgroundRepeat: 'no-repeat', 
      backgroundPosition: 'center', 
      height: '100vh', 
      width: '100vw' }}>
    <div className="auth-page-container">
      <div className="hero-div">
      {<h2>{user.id ? "EDITAR USUARIO" : "AÑADIR USUARIO"}</h2>}
      {modal}
      <div className="auth-form-container">
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
              onChange={handleUserChange}
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
            />
          </div>
          <div className="custom-form-input">
            <Label for="firstName" className="custom-form-input-label">
              FirstName
            </Label>
            <Input
              type="text"
              required
              name="firstName"
              id="firstName"
              value={player.firstName || ""}
              onChange={handlePlayerChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="lastName" className="custom-form-input-label">
              LastName
            </Label>
            <Input
              type="text"
              required
              name="lastName"
              id="lastName"
              value={player.lastName || ""}
              onChange={handlePlayerChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="email" className="custom-form-input-label">
              Email
            </Label>
            <Input
              type="text"
              name="email"
              id="email"
              value={player.email || ""}
              onChange={handlePlayerChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="photo" className="custom-form-input-label">
              Foto
            </Label>
            <Input
              type="text"
              name="photo"
              id="photo"
              value={player.photo || ""}
              onChange={handlePlayerChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="authority" className="custom-form-input-label">
              Autoridad
            </Label>
              <Input
                type="select"
                required
                name="authority"
                id="authority"
                value={user.authority?.id || ""}
                onChange={handleUserChange}
                className="custom-input"
              >
                <option value="">None</option>
                {authOptions}
              </Input>
          </div>
          <div className="custom-button-row">
            <button className="auth-button">Guardar</button>
            <Link
              to={`/users`}
              className="auth-button"
              style={{ textDecoration: "none" }}
            >
              Cancelar
            </Link>
          </div>
        </Form>
      </div>
      </div>
    </div>
    </div>
  );
}