import { Form, Label} from "reactstrap";
import { IoCloseCircle } from "react-icons/io5";


export default function getCreationModal(setModalVisible, modalVisible=false){

    function handleModalVisible(setModalVisible, modalVisible) {
        setModalVisible(!modalVisible);
    }

    return (
        <div className= 'cuadro-creacion'>
            <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: "absolute" }} onClick={() => handleModalVisible(setModalVisible, modalVisible)} />
             <Form>
                <h3> Ajuste de la partida: </h3>
                        <div>
                            
                        </div>
                        
                        <div>
                            <Label>
                                Con Flor:
                            </Label>
                            </div>
                            <div>
                            <Label>
                                Partida privada:
                            </Label>
                            </div>
                            
                            <div className="botones">
                            <button className="recuadro">Crear</button>
                            <button className="recuadro">Volver</button>
                            </div>
                    </Form>
            </div>
    )



}