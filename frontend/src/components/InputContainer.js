

const InputContainer = ({ mensaje, setMensaje, evtEnviarMensaje }) => {
    return (
        <div className="input-container">
            <input
                type="text"
                value={mensaje}
                onChange={(e) => setMensaje(e.target.value)}
                placeholder="Escribe un mensaje..."
                className="input-text"
                onKeyPress={(e) => {
                    if (e.key === "Enter") {
                        evtEnviarMensaje();
                    }
                }}
            />
            <button onClick={evtEnviarMensaje} className="btn-send">
                Enviar
            </button>
        </div>
    );
}
export default InputContainer;