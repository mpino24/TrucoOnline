import { useState } from "react";
import { Table } from "reactstrap";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import { useNavigate } from 'react-router-dom';

const jwt = tokenService.getLocalAccessToken();

export default function PartidasAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [gamesData, setGamesData] = useFetchState(
      {},
      `/api/v1/partida/partidas/paginadas?page=0&size=6`,
      jwt,
      setMessage,
      setVisible
    );

  const navigate = useNavigate();
  const handleRedirect = (path) => {
      navigate(path);
  };

  const [paginaActual, setPaginaActual] = useState(0);

  const partidasPorPagina = 6;
  const games = gamesData.content || [];
  const totalPaginas = gamesData.totalPages || 1;

  const handlePaginaSiguiente = () => {
    if (paginaActual < totalPaginas - 1) {
      const nuevaPagina = paginaActual + 1;
      fetchPartidasPaginadas(nuevaPagina);
      setPaginaActual(nuevaPagina);
    }
  };

  const handlePaginaAnterior = () => {
    if (paginaActual > 0) {
      const nuevaPagina = paginaActual - 1;
      fetchPartidasPaginadas(nuevaPagina);
      setPaginaActual(nuevaPagina);
    }
  };

  const fetchPartidasPaginadas = (pagina) => {
    fetch(`/api/v1/partida/partidas/paginadas?page=${pagina}&size=${partidasPorPagina}`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((res) => res.json())
      .then((data) => setGamesData(data))
      .catch((error) => setMessage("Error al cargar partidas"));
  };

  const gameList = games.map((game) => (
    <tr key={game.id}>
      <td>{game.codigo}</td>
      <td>{game.participantes}</td>
      <td>{game.creador}</td>
      <td>{game.visibilidad}</td>
      <td>{game.tipo}</td>
    </tr>
  ));

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div
      style={{
        backgroundImage: "url(/fondos/fondo_admin.png)",
        backgroundSize: "cover",
        backgroundRepeat: "no-repeat",
        backgroundPosition: "center",
        height: "100vh",
        width: "100vw",
      }}
    >
      <div expand='md' style={{ marginLeft: '80%' }}>
        <button className="button-admin" onClick={() => { navigate("/admin") }}>
          VOLVER
        </button>
      </div>
        <div className="hero-div" style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center' }}>
          <h1 className="text-center">PARTIDAS</h1>
          {modal}
          <div>
            <Table aria-label="games" className="admin-table mt-4">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Participantes</th>
                  <th>Creador</th>
                  <th>Visibilidad</th>
                  <th>En curso/Terminada</th>
                </tr>
              </thead>
              <tbody>{gameList}</tbody>
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