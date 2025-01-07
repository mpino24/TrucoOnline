import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";

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
            <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + user.id}
              tag={Link}
              to={"/users/" + user.id}
            >
              Editar
            </Button>
            <Button
              size="sm"
              color="danger"
              aria-label={"delete-" + user.id}
              onClick={() =>
                deleteFromList(
                  `/api/v1/users/${user.id}`,
                  user.id,
                  [users, setUsersData],
                  [alerts, setAlerts],
                  setMessage,
                  setVisible
                )
              }
            >
              Borrar
            </Button>
          </ButtonGroup>
        </td>
      </tr>
    );
  });

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="admin-page-container">
      <h1 className="text-center">Usuarios</h1>
      {alerts.map((a) => a.alert)}
      {modal}
      <Button color="success" tag={Link} to="/users/new">
        Añadir usuario
      </Button>
      <div>
        <Table aria-label="users" className="mt-4">
          <thead>
            <tr>
              <th>Username</th>
              <th>Authority</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>{userList}</tbody>
        </Table>
      </div>
      <div className="pagination-controls">
        <Button
          color="primary"
          size="sm"
          onClick={handlePaginaAnterior}
          disabled={paginaActual === 0}
        >
          Anterior
        </Button>
        <span className="pagination-info">
          Página {paginaActual + 1} de {totalPaginas}
        </span>
        <Button
          color="primary"
          size="sm"
          onClick={handlePaginaSiguiente}
          disabled={paginaActual === totalPaginas - 1}
        >
          Siguiente
        </Button>
      </div>
    </div>
  );
}
