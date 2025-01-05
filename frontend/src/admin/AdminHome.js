import { useState } from "react";
import { Table } from "reactstrap";
import "../static/css/admin/adminPage.css";
import getErrorModal from "../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import { useNavigate } from 'react-router-dom';

export default function AdminHome() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);
    const [games, setGames] = useFetchState([], "/api/v1/partida/partidas/participantes/activas");
    const gameList = games.map((game) => {
        return (
          <tr key={game.id}>
            <td>{game.codigo}</td>
            <td>{game.participantes}</td>
            <td>{game.creador}</td>
          </tr>
        );
      });
    const modal = getErrorModal(setVisible, visible, message);
    const navigate = useNavigate();
    const handleRedirect = (path) => {
        navigate(path);
    };

    return (
      <div style={{ backgroundImage: 'url(/fondos/fondo_admin.png)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw' }}>
        <div className="admin-page-container">
            <div className="hero-div" style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center' }}>
              <h1>ADMINISTRAR</h1>
                <button className="home-button" onClick={() => {navigate("/users")}}>
                USUARIOS
                </button>
                <button className="home-button" onClick={() => {navigate("/admin/partidas")}}>
                PARTIDAS EN CURSO
                </button>
                <button className="home-button" onClick={() => {navigate("/admin/partidas/terminadas")}}>
                PARTIDAS JUGADAS
                </button>
                <button className="home-button" onClick={() => {navigate("/admin/estadisticas")}}>
                ESTADÍSTICAS
                </button>
            </div>
        </div>
      </div>
    );
}