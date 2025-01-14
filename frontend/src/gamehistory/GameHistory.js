import { useState } from "react";
import { Table } from "reactstrap";
import "../static/css/admin/adminPage.css";
import getErrorModal from "../util/getErrorModal";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import { useNavigate } from 'react-router-dom';

const jwt = tokenService.getLocalAccessToken();

export default function GameHistory() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [gamesData, setGamesData] = useFetchState(
      {},
      `https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/partida/partidas/historial?page=0&size=6`,
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
    fetch(`https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/partida/partidas/historial?page=${pagina}&size=${partidasPorPagina}`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((res) => res.json())
      .then((data) => setGamesData(data))
      .catch((error) => setMessage("Error al cargar partidas"));
  };

  const formatearFechaHora = (item) => {
    const fecha = new Date(item);
    const dia = fecha.getDay();
    const mes = fecha.getMonth();
    const anio = fecha.getFullYear();
    const hora = fecha.getHours();
    const minutos = fecha.getMinutes();
    return `${dia}/${mes}/${anio} ${hora}:${minutos}`;
  }
 
  const gameList = games.map((game) => (
    
    <tr key={game.id}>
      <td>{game.codigo}</td>
      <td>{game.participantes}</td>
      <td>{game.creador}</td>
      <td>{game.visibilidad}</td>
      <td>{formatearFechaHora(game.inicio)}</td>
      <td>{formatearFechaHora(game.fin)}</td>
    </tr>
  ));

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div
      style={{
        backgroundImage: "url(/fondos/fondologin.jpg)",
        backgroundSize: "cover",
        backgroundRepeat: "no-repeat",
        backgroundPosition: "center",
        height: "100vh",
        width: "100vw",
      }}
    >
      <div expand='md' style={{ marginLeft: '80%'}}>
        <button style={{marginTop:"20px"}}className="back-button" onClick={() => { navigate("/home") }}>
          VOLVER
        </button>
      </div>
        <div className="hero-div" style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center' }}>
          <h1 className="loginText" style={{color: "rgb(255, 223, 65)"}}>HISTORIAL DE PARTIDAS</h1>
          {modal}
          <div>
            <Table aria-label="games" className="admin-table mt-4">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Participantes</th>
                  <th>Creador</th>
                  <th>Visibilidad</th>
                  <th>Inicio</th>
                  <th>Fin</th>
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