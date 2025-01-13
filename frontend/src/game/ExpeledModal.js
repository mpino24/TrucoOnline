import { forwardRef } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom'



const ExpeledModal = forwardRef((props, ref) => {
    const navigate = useNavigate();


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
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.6)',
            zIndex: 1000 
        }
    };



    return (
        <Modal
            isOpen={props.modalIsOpen}
            style={customModalStyles}
            ariaHideApp={false}
        >
            <h2 style={{ marginBottom: '20px', color: '#333' }}>Has sido expulsado de la partida</h2>
            <img src="https://c.tenor.com/sPn8-fZm8TsAAAAd/tenor.gif" alt="Mono GIF" style={{ width: '100%', height: 'auto', marginBottom: '20px' }} />
            <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                <button
                    onClick={(()=> {
                        navigate("/home")
                    })}
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
                    Salir de la partida
                </button>
            </div>
        </Modal>
    );
});
export default ExpeledModal;