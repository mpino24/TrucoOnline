import { useState, useCallback, useEffect } from "react";
import { Table } from "reactstrap";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import LogroComponent from "../../estadisticas/LogroComponent";
import CreationLogroModal from './getCreationLogroModal.js';
import EditLogroModal from "./EditLogroModal.js";  // Importando el modal de edición
import { FaTrashAlt, FaPencilAlt } from "react-icons/fa";

const jwt = tokenService.getLocalAccessToken();

export default function EstadisticasAdmin() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);
    const [creationLogroModal, setCreationLogroModal] = useState(false);
    const [editLogroModal, setEditLogroModal] = useState(false);
    const [logroSeleccionado, setLogroSeleccionado] = useState(null);
    const [actualizarLista, setActualizarLista] = useState(0);
    const [listaLogrosGlobales, setListaLogrosGlobales] = useFetchState([], "/api/v1/logros", jwt, setMessage, setVisible);
    const toggleCreationLogroModal = useCallback(() => {
        setCreationLogroModal((current) => !current);
    }, []);

    const toggleEditLogroModal = useCallback(() => {
        setEditLogroModal((current) => !current);
    }, []);


    function handleDelete(id) {
        fetch("/api/v1/logros/" + id, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${jwt}`,
            },
        })
            .then((response) => response.text())
            .then((data) => {
                setActualizarLista(actualizarLista + 1);
            })
            .catch((message) => alert(message));
    };

    const handleEdit = (logro) => {
        setLogroSeleccionado(logro);
        toggleEditLogroModal();
    };

    useEffect(() => {
        fetch("/api/v1/logros", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${jwt}`,
            },
        })
            .then((response) => response.json())
            .then((data) => {
                if (data) {
                    setListaLogrosGlobales(data);
                }
            })
            .catch((message) => alert("Error: " + message));
    }, [actualizarLista]);

    return (
        <div style={{ backgroundImage: 'url(/fondos/fondo_admin.png)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw' }}>
            <div className="admin-page-container">
                <h1 className="text-center" style={{ marginBottom: '30px', marginTop: '50px', color: 'white', backgroundColor: 'rgb(0,0,0,0.2)' }}>Logros creados</h1>

                <div style={logrosGridStyle}>
                    {listaLogrosGlobales.map((logro, index) => (
                        <div key={index} style={logroCardStyle}>
                            {logro?.id !== logroSeleccionado?.id && (<>
                                <LogroComponent logro={logro} />
                                <FaTrashAlt style={{marginRight:'20px'}} onMouseEnter={(e) => {
                                    e.target.style.color = 'darkred';
                                    e.target.style.transform = 'scale(1.05)';
                                }}
                                    onMouseLeave={(e) => {
                                        e.target.style.color = 'red';
                                        e.target.style.transform = 'scale(1)';
                                    }} color='red' onClick={() => handleDelete(logro.id)} />
                                <FaPencilAlt onMouseEnter={(e) => {
                                    e.target.style.color = 'rgb(255, 255,255,0.8)';
                                    e.target.style.transform = 'scale(1.05)';
                                }}
                                    onMouseLeave={(e) => {
                                        e.target.style.color = 'white';
                                        e.target.style.transform = 'scale(1)';
                                    }} color='white' onClick={() => handleEdit(logro)} />
                            </>)}
                            {editLogroModal && logroSeleccionado?.id === logro?.id && (
                                <>{console.log("soy este logro: " + logro.id)}
                                    <EditLogroModal
                                        setEditLogroModal={setEditLogroModal}
                                        editLogroModal={editLogroModal}
                                        logro={logroSeleccionado}
                                        setActualizarLista={setActualizarLista}

                                        actualizarLista={actualizarLista}
                                        setLogroSeleccionado={setLogroSeleccionado}
                                    />
                                </>
                            )}
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
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '100%',
    border: '1px solid #ddd',
    borderRadius: '10px',
    padding: '20px',
    backgroundColor: 'rgba(145, 139, 139, 0.2)',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    textAlign: 'center',
    position: 'relative',
};

const logrosGridStyle = {
    display: 'grid',
    gridTemplateColumns: 'repeat(4, 2fr)',
    gap: '20px',
    maxHeight: '700px',
    overflowY: 'auto',
    padding: '10px',
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

const botonEstilo = {
    width: '100%',
    height: '100%',
    border: '1px solid #ddd',
    borderRadius: '10px',
    padding: '20px',
    backgroundColor: 'rgba(145, 139, 139, 0.2)',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    cursor: 'pointer',
    fontSize: '18px',
    fontWeight: 'bold',
    transition: 'background-color 0.3s, transform 0.3s',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};
