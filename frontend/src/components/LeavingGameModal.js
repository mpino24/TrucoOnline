import { forwardRef } from 'react';
import Modal from 'react-modal';
import tokenService from '../services/token.service.js';
import { useNavigate } from 'react-router-dom'



const LeavingGameModal = forwardRef((props, ref) => {
    const navigate = useNavigate();
    const jwt = tokenService.getLocalAccessToken();
    function closeModal() {
        props.setIsOpen(false);
    }

    function leaveGame() {
        fetch(
            "/api/v1/partidajugador/salir",
            {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                  }
            }
        )
            .then((response) => response.text())
            .then((data) => {
                closeModal();
                navigate("/home");
            })
            .catch((message) => alert(message));
    }

    const customModalStyles = {
        content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            width: '400px',
            padding: '20px',
            borderRadius: '10px',
            boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
            textAlign: 'center',
            border: 'none',

        },
        overlay: {
            backgroundColor: 'rgba(0, 0, 0, 0.6)'
        }
    };



    return (
        <Modal
            isOpen={props.modalIsOpen}
            onRequestClose={closeModal}
            style={customModalStyles}
            ariaHideApp={false}
        >
            <h2 style={{ marginBottom: '20px', color: '#333' }}>¿Quieres abandonar la partida?</h2>
            <img src="https://c.tenor.com/vkvU9Fi4uOsAAAAC/tenor.gif" alt="Mono GIF" style={{ width: '100%', height: 'auto', marginBottom: '20px' }} />
            <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                <button
                    onClick={leaveGame}
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#ff4d4d',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontSize: '16px',
                        transition: 'background-color 0.3s ease',
                        marginRight: '10px'
                    }}
                    onMouseEnter={(e) => (e.target.style.backgroundColor = '#ff3333')}
                    onMouseLeave={(e) => (e.target.style.backgroundColor = '#ff4d4d')}
                >
                    Sí, abandonar partida
                </button>
                <button
                    onClick={closeModal}
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#4CAF50',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontSize: '16px',
                        transition: 'background-color 0.3s ease'
                    }}
                    onMouseEnter={(e) => (e.target.style.backgroundColor = '#45a049')}
                    onMouseLeave={(e) => (e.target.style.backgroundColor = '#4CAF50')}
                >
                    No, me quedo
                </button>
            </div>
        </Modal>
    );
});
export default LeavingGameModal;