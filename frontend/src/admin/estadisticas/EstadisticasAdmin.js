import { useState, useCallback, useEffect } from "react";
import { Table } from "reactstrap";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import LogroComponent from "../../estadisticas/LogroComponent";
import CreationLogroModal from  './getCreationLogroModal.js';
import { FaTrashAlt } from "react-icons/fa";

const jwt = tokenService.getLocalAccessToken();



export default function EstadisticasAdmin() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);
    const [creationLogroModal, setCreationLogroModal] = useState(false);
    const [actualizarLista, setActualizarLista] = useState(0);
    const [listaLogrosGlobales, setListaLogrosGlobales] = useFetchState([], "/api/v1/logros", jwt, setMessage, setVisible);
    console.log(listaLogrosGlobales)

    const toggleCreationLogroModal = useCallback(() => {
            setCreationLogroModal((current) => !current);
        }, []);

    function handleDelete(id) {
            fetch(
                "/api/v1/logros/"+ id,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                      },
                }
            )
                .then((response) => response.text())
                .then((data) => {
                    setActualizarLista(actualizarLista+1);
                })
                .catch((message) => alert(message));
        };

        useEffect(() => {
            fetch(
                `/api/v1/logros`,
                {
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${jwt}`,
                    }
                }
            )
                .then((response) => response.json())
                .then((data) => {
                    if (data) {
                        setListaLogrosGlobales(data)
                       
                    }
                })
                .catch((message) => alert("Error: " + message));
        }, [actualizarLista]);

    return (
      <div style={{ backgroundImage: 'url(/fondos/fondo_admin.png)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw' }}>
        <div className="admin-page-container">
            <h1 className="text-center">Logros creados</h1>
            
            <div style={logrosGridStyle}>
                {listaLogrosGlobales.map((logro, index) => (
                    <div key={index} style={logroCardStyle}>
                        <LogroComponent logro={logro} />
                        {console.log(logro.id)}
                        <FaTrashAlt onClick={() => handleDelete(logro.id)}/>
                    </div>
                ))}

                
                <div style={gridBoton}>
                    {!creationLogroModal && <button style={botonEstilo} onClick={toggleCreationLogroModal}>Crear nuevo logro</button>}
                    {creationLogroModal &&
                            <CreationLogroModal setCreationLogroModal={setCreationLogroModal} creationLogroModal={creationLogroModal} setActualizarLista={setActualizarLista} actualizarLista={actualizarLista} />
                        }
                </div>
                
            </div>
        </div>
      </div>
    );
}

const gridBoton = {
    display: 'flex',
    justifyContent: 'center', // Centra el contenido horizontalmente
    alignItems: 'center', // Centra el contenido verticalmente
    width: '100%', // Hace que el contenedor ocupe todo el espacio disponible
    height: '100%', // Hace que el contenedor ocupe todo el espacio del grid
    border: '1px solid #ddd',
    borderRadius: '10px',
    padding: '20px', // Ajusta el padding para que el botón tenga espacio
    backgroundColor: 'rgba(145, 139, 139, 0.2)',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    textAlign: 'center',
    position: 'relative',
};

const logrosGridStyle = {
    display: 'grid',
    gridTemplateColumns: 'repeat(4, 1fr)', 
    gap: '20px',
    maxHeight: '600px',
    overflowY: 'auto',
    padding: '20px',
    backgroundColor: 'rgba(255, 255, 255, 0)',
    borderRadius: '10px',
    scrollbarWidth: 'none', 
    msOverflowStyle: 'none', 
};

const logroCardStyle = {
    border: '1px solid #ddd',
    borderRadius: '10px',
    padding: '10px',
    backgroundColor: 'rgba(145, 139, 139, 0.2)',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    textAlign: 'center',
    position: 'relative',
};

// Estilo para el botón
const botonEstilo = {
    width: '100%', // Hace que el botón ocupe todo el ancho del contenedor
    height: '100%', // Hace que el botón ocupe todo el alto del contenedor
    border: '1px solid #ddd',
    borderRadius: '10px',
    padding: '20px', // Hace el botón más grande con más padding
    backgroundColor: 'rgba(145, 139, 139, 0.2)',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    cursor: 'pointer',
    fontSize: '18px', // Aumenta el tamaño de la fuente
    fontWeight: 'bold',
    transition: 'background-color 0.3s, transform 0.3s', // Animación de hover
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

// Estilo para el botón en hover
const botonEstiloHover = {
    ...botonEstilo,
    backgroundColor: 'rgba(145, 139, 139, 0.4)', // Cambio de color en hover
    transform: 'scale(1.05)', // Agranda el botón ligeramente
};
