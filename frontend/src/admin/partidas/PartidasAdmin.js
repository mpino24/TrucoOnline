import { useState } from "react";
import { Table } from "reactstrap";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";
export default function PartidasAdmin() {
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
    return (
      <div style={{ backgroundImage: 'url(/fondos/fondo_admin.png)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw' }}>
        <div className="admin-page-container">
            <h1 className="text-center">Partidas en curso</h1>
            {alerts.map((a) => a.alert)}
            {modal}
            <div>
                <Table aria-label="games" className="mt-4">
                <thead>
                    <tr>
                    <th>Codigo</th>
                    <th>Participantes</th>
                    <th>Creador</th>
                    </tr>
                </thead>
                <tbody>{gameList}</tbody>
                </Table>
            </div>
        </div>
      </div>
    );
}