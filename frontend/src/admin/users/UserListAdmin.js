import { useState } from "react";
import { ButtonGroup, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";
import { useNavigate } from 'react-router-dom';

const jwt = tokenService.getLocalAccessToken();

export default function UserListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [usersData, setUsersData] = useFetchState(
    {},
    `/api/v1/users/paginados?page=0&size=6`,
    jwt,
    setMessage,
    setVisible
  );

  const navigate = useNavigate();
  const handleRedirect = (path) => {
    navigate(path);
  };
  
  const [paginaActual, setPaginaActual] = useState(0);
  const [alerts, setAlerts] = useState([]);

  const usuariosPorPagina = 6;

  const users = usersData.content || [];
  const totalPaginas = usersData.totalPages || 1;

  const handlePaginaSiguiente = () => {
    if (paginaActual < totalPaginas - 1) {
      const nuevaPagina = paginaActual + 1;
      fetchUsuariosPaginados(nuevaPagina);
      setPaginaActual(nuevaPagina);
    }
  };

  const handlePaginaAnterior = () => {
    if (paginaActual > 0) {
      const nuevaPagina = paginaActual - 1;
      fetchUsuariosPaginados(nuevaPagina);
      setPaginaActual(nuevaPagina);
    }
  };

  const fetchUsuariosPaginados = (pagina) => {
    fetch(`/api/v1/users/paginados?page=${pagina}&size=${usuariosPorPagina}`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((res) => res.json())
      .then((data) => setUsersData(data))
      .catch((error) => setMessage("Error al cargar usuarios"));
  };

  const userList = users.map((user) => {
    const { username } = user;
    const { authority } = user.authority;

    return (
      <tr key={user.id}>
        <td>{username}</td>
        <td>{authority}</td>
        <td>
          <ButtonGroup>
            <button
              className="admin-home-button-edit"
              aria-label={"edit-" + user.id}
              onClick={() => { navigate("/users/"+user.id) }}
            >
              Editar
            </button>
            <button
              className="admin-home-button-delete"
              aria-label={"delete-" + user.id}
              onClick={() => {
                deleteFromList(
                  `/api/v1/jugador/${user.id}`,
                  user.id,
                  [users, setUsersData],
                  [alerts, setAlerts],
                  setMessage,
                  setVisible
                ); navigate("/admin")
                }
              }
            >
              Borrar
            </button>
          </ButtonGroup>
        </td>
      </tr>
    );
  });

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div style={{ 
      backgroundImage: 'url(/fondos/fondo_admin.png)', 
      backgroundSize: 'cover', 
      backgroundRepeat: 'no-repeat', 
      backgroundPosition: 'center', 
      height: '100vh', 
      width: '100vw' 
      }}>
      <div expand='md' style={{ marginLeft: '80%' }}>
          <button className="button-admin" onClick={() => { navigate("/admin") }}>
            VOLVER
        </button>
      </div>
      <div className="hero-div" style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center' }}>
        <h1 className="text-center">USUARIOS</h1>
        {alerts.map((a) => a.alert)}
        {modal}
        <button className="admin-home-button-1" onClick={() => {navigate("/users/new")}}>
          Añadir usuario
        </button>
        <div>
          <Table aria-label="users" className="admin-table mt-4">
            <thead>
              <tr>
                <th>Username</th>
                <th>Autoridad</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>{userList}</tbody>
          </Table>
        </div>
        <div className="pagination-controls">
          <button
            className="admin-home-button-1"
            onClick={handlePaginaAnterior}
            disabled={paginaActual === 0}
            style={{ visibility: paginaActual === 0 ? "hidden" : "visible" }}
          >
            &lt;
          </button>
          <span className="pagination-info">
            {paginaActual + 1} de {totalPaginas}
          </span>
          <button
            className="admin-home-button-1"
            onClick={handlePaginaSiguiente}
            disabled={paginaActual === totalPaginas - 1}
            style={{ visibility: paginaActual === totalPaginas - 1 ? "hidden" : "visible" }}
          >
            &gt;
          </button>
        </div>
      </div>
    </div>
  );
}
