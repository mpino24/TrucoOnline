import { useState } from "react";
import "../static/css/admin/adminPage.css";
import getErrorModal from "../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import { useNavigate } from 'react-router-dom';
import tokenService from "../services/token.service";

export default function AdminHome() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);
    const jwt = tokenService.getLocalAccessToken();
   
  const modal = getErrorModal(setVisible, visible, message);

    const navigate = useNavigate();
  
    return (
      <div style={{ backgroundImage: 'url(/fondos/fondo_admin.png)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw' }}>
        <div className="admin-page-container">
            <div className="hero-div" style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center' }}>
              <h1>ADMINISTRACIÓN</h1>
                <button className="home-button" onClick={() => {navigate("/users")}}>
                USUARIOS
                </button>
                <button className="home-button" onClick={() => {navigate("/admin/partidas")}}>
                PARTIDAS
                </button>
                <button className="home-button" onClick={() => {navigate("/admin/estadisticas")}}>
                ESTADÍSTICAS
                </button>
            </div>
        </div>
      </div>
    );
}