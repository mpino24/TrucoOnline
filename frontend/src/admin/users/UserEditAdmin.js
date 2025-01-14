import { useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchState from "../../util/useFetchState";
import { useNavigate } from "react-router-dom";
import SelectorImagenes from "../../util/SelectorImagenes";
const jwt = tokenService.getLocalAccessToken();

export default function UserEditAdmin() {
  const emptyUser = {
    id: null,
    username: "",
    password: "",
    authority: {id:2, authority:"PLAYER"},
    isConnected: false
  };
  const emptyPlayer = {
    firstName: "",
    lastName: "",
    email: "",
    photo: "https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/perfiles/robot.jpg"
  }
  const [newPassword, setNewPassword] = useState();
  const id = getIdFromUrl(2);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [player, setPlayer] = useFetchState(
    emptyPlayer,
    `https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/jugador/edit/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  const [authorities, setAuthorities] = useFetchState(
    [],
    "https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/users/authorities",
    jwt,
    setMessage,
    setVisible
  );
  const navigate = useNavigate();
  const [user, setUser] = useFetchState(
    emptyUser,
    `https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/users/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
const [imageModalOpen, setImageModalOpen] = useState(false);

  const [imagenesDisponibles, setImagenesDisponibles] = useFetchState([], "/api/v1/fotos/perfiles", jwt, setMessage, setVisible);

  const handleImageSelect = (imageName) => {
    setPlayer({
      ...player,
      photo: `https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/perfiles/${imageName}`,
    });
    setImageModalOpen(false);
  };
  function handleUserChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    if (name === "authority") {
      if(value === "ADMIN"){
        setUser({ ...user, authority: authorities[0] });
      }else{
        setUser({ ...user, authority: authorities[1] });
      }

    } else setUser({ ...user, [name]: value });
  }

  function handlePlayerChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setPlayer({ ...player, [name]: value });
  }

  function handleSubmit(event) {
    event.preventDefault();

    const request = {
      username: user.username,
      password: user.password,
      authority: user.authority.authority, 
      firstName: player.firstName,
      lastName: player.lastName,
      email: player.email,
      photo: player.photo,
    };

    user.password = newPassword ? newPassword : undefined;

    const endpoint = user.id ? `/api/v1/users/${user.id}` : "/api/v1/auth/signup";
    const method = user.id ? "PUT" : "POST";
    const body = user.id ? JSON.stringify(user) : JSON.stringify(request);

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
            navigate("/users");
          }
        }
      })
      .then((response) => response?.json())
      .then((json) => {
        if (json?.message) {
          setMessage(json.message);
          setVisible(true);
        } else {
          navigate("/users");
        }
      })
      .catch((error) => alert(error.message));
  }

  const modal = getErrorModal(setVisible, visible, message);

  function handlePasswordChange(event) {
    const newPass = event.target.value;
    setNewPassword(newPass);
    setUser({ ...user, password: newPass });
  }

  return (
    <div
      style={{
        backgroundImage: 'url(/fondos/fondo_admin.png)',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        height: '100vh',
        width: '100vw',
      }}
    >
      <div className="auth-page-container" >
        <div className="hero-div" style={{width:'50%', top:'55px', position:'absolute' }}>
          {<h2>{user.id ? "EDITAR USUARIO" : "AÑADIR USUARIO"}</h2>}
          {modal}
          <div className="auth-form-container" >
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
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <Label >Foto:  </Label>
          {player.photo && (
            <img
              src={player.photo}
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
              <div className="custom-form-input">
                <Label for="authority" className="custom-form-input-label">
                  Autoridad
                </Label>
                <Input
                  type="select"
                  required
                  name="authority"
                  id="authority"
                  value={user?.authority?.authority || ""}
                  onChange={handleUserChange}
                  className="custom-input"
                >
                  {authorities?.map((auth, index) => (
                    <option key={index} value={auth.authority}>
                      {auth.authority}
                    </option>
                  ))}
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
            <SelectorImagenes
        imagenes={imagenesDisponibles}
        isOpen={imageModalOpen}
        toggle={() => setImageModalOpen(!imageModalOpen)}
        onSelect={handleImageSelect}
        tipo={"perfil"}
      />
          </div>
        </div>
      </div>
    </div>
  );
}
