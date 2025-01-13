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
                  },
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
            border: "2px solid rgb(255, 170, 66)",
            backgroundColor:"rgba(255, 214, 187, 0.678)",
            borderRadius: "30px",
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            width: '400px',
            padding: '20px',
            borderRadius: '20px',
            boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
            textAlign: 'center',
            zIndex: 1003242343240

        },
        overlay: {
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.6)',
            zIndex: 10423432432400 
        }
    };



    return (
        <Modal
            isOpen={props.modalIsOpen}
            onRequestClose={closeModal}
            style={customModalStyles}
            ariaHideApp={false}
        >
            <h2 className="loginText" style={{ marginBottom: '20px' }}>¿Quieres abandonar la partida?</h2>
            <img src="https://c.tenor.com/vkvU9Fi4uOsAAAAC/tenor.gif" alt="Mono GIF" style={{ width: '100%', height: 'auto', marginBottom: '20px' }} />
            <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                <button
                    onClick={leaveGame}
                    className='dangerButton2'
                    style={{
                        padding: '10px 20px',
                        cursor: 'pointer',
                        fontSize: '16px',
                        marginRight: '10px'
                    }}
                
                >
                    Sí, abandonar partida
                </button>
                <button
                className='heavenButton'
                    onClick={closeModal}
                    style={{
                        padding: '10px 20px',
                        cursor: 'pointer',
                        fontSize: '16px',
                    }}
                    
                >
                    No, me quedo
                </button>
            </div>
        </Modal>
    );
});
export default LeavingGameModal;