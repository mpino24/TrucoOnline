import { useState, useEffect } from "react";
import { Table } from "reactstrap";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import tokenService from "../../services/token.service";
import { useNavigate } from 'react-router-dom';

export default function PartidasAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [games, setGames] = useState([]);

  const jwt = tokenService.getLocalAccessToken();

  const navigate = useNavigate();
  const handleRedirect = (path) => {
      navigate(path);
  };

  useEffect(() => {
    const fetchGames = async () => {
      try {
        const response = await fetch("/api/v1/partida/partidas/participantes/activas", {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        });

        if (!response.ok) {
          throw new Error(`Error: ${response.statusText}`);
        }
        const data = await response.json();
        setGames(data);
      } catch (error) {
        console.error("Fetch error:", error);
        setMessage(error.message);
        setVisible(true);
      }
    };

    fetchGames();
  }, [jwt]);

  const gameList = games.map((game) => (
    <tr key={game.id}>
      <td>{game.codigo}</td>
      <td>{game.participantes}</td>
      <td>{game.creador}</td>
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
      <div expand='md' style={{ float: 'left' }}>
        <button className="button-admin" onClick={() => { navigate("/admin") }}>
          VOLVER
        </button>
      </div>
      <div className="admin-page-container">
        <div className="hero-div" style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center' }}>
          <h1 className="text-center">PARTIDAS EN CURSO</h1>
          {modal}
          <div>
            <Table aria-label="games" className="admin-table mt-4">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Participantes</th>
                  <th>Creador</th>
                </tr>
              </thead>
              <tbody>{gameList}</tbody>
            </Table>
          </div>
        </div>
      </div>
    </div>
  );
}