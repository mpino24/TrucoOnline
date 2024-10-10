import { Form, Label} from "reactstrap";
import { IoCloseCircle } from "react-icons/io5";



export default function getCreationModal(setModalVisible, modalVisible=false){



    function handleModalVisible(setModalVisible, modalVisible) {
        setModalVisible(!modalVisible);
    }



    return (

        <div >
            <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: "absolute" }} onClick={() => handleModalVisible(setModalVisible, modalVisible)} />
            <div className= 'cuadro-creacion'>
                <div style={{marginTop:"20px"}}>
             <Form >
                
                <h3> Ajuste de la partida: </h3>
                <div className='contenedor'>
                    <Label> Numero de jugadores: </Label>
                <div class="paste-button">
                    
                    <button class="button"> &nbsp; ▼</button>
                    <div class="dropdown-content">
                        <a id="top" href="#">2</a>
                        <a id="middle" href="#">4</a>
                        <a id="bottom" href="#">6</a>
                    </div>
                </div>
                </div>
                
                <div className='contenedor'>
                            <Label>
                             Puntos:
                            </Label>
                                <p style={{ marginRight:"10px",marginLeft:"170px"}}>15</p>
                                <div class="checkbox-wrapper-51">

                                    <input id="cbx-51" type="checkbox"/>
                                    <label class="toggle" for="cbx-51">
                                        <span>
                                        <svg viewBox="0 0 10 10" height="10px" width="10px">
                                            <path d="M5,1 L5,1 C2.790861,1 1,2.790861 1,5 L1,5 C1,7.209139 2.790861,9 5,9 L5,9 C7.209139,9 9,7.209139 9,5 L9,5 C9,2.790861 7.209139,1 5,1 L5,9 L5,1 Z"></path>
                                        </svg>
                                        </span>
                                    </label>
                                    </div>
                                    <p style={{marginRight:"10px",marginLeft:"10px"}}>30</p>

                        </div>
                            


                    <div className='contenedor'>
                            <Label>
                             Usar Flor:
                            </Label>
                                <p style={{ marginRight:"10px",marginLeft:"150px"}}>no</p>
                                <div class="checkbox-wrapper-51">

                                    <input id="cbx-52" type="checkbox"/>
                                    <label class="toggle" for="cbx-52">
                                        <span>
                                        <svg viewBox="0 0 10 10" height="10px" width="10px">
                                            <path d="M5,1 L5,1 C2.790861,1 1,2.790861 1,5 L1,5 C1,7.209139 2.790861,9 5,9 L5,9 C7.209139,9 9,7.209139 9,5 L9,5 C9,2.790861 7.209139,1 5,1 L5,9 L5,1 Z"></path>
                                        </svg>
                                        </span>
                                    </label>
                                    </div>
                                    <p style={{marginRight:"10px",marginLeft:"10px"}}>si</p>

                        </div>


                        <div className='contenedor'>
                            <Label>
                             Partida privada:
                            </Label>
                                <p style={{ marginRight:"10px",marginLeft:"100px"}}>no</p>
                                <div class="checkbox-wrapper-51">

                                    <input id="cbx-53" type="checkbox"/>
                                    <label class="toggle" for="cbx-53">
                                        <span>
                                        <svg viewBox="0 0 10 10" height="10px" width="10px">
                                            <path d="M5,1 L5,1 C2.790861,1 1,2.790861 1,5 L1,5 C1,7.209139 2.790861,9 5,9 L5,9 C7.209139,9 9,7.209139 9,5 L9,5 C9,2.790861 7.209139,1 5,1 L5,9 L5,1 Z"></path>
                                        </svg>
                                        </span>
                                    </label>
                                    </div>
                                    <p style={{marginRight:"10px",marginLeft:"10px"}}>si</p>

                        </div>

                            <div className="botones">
                            <button className="recuadro">Crear</button>
                            <button className="recuadro" onClick={() => handleModalVisible(setModalVisible, modalVisible)}>Volver</button>
                            </div>
                    </Form>
                    </div>
            </div>
            </div>
    )



}