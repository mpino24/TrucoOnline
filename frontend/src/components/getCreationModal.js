import { forwardRef, useState } from 'react';
import { Form, Label} from "reactstrap";
import useFetchState from "../util/useFetchState.js";
import { IoCloseCircle } from "react-icons/io5";
import tokenService from "../services/token.service.js";
import getErrorModal from "../util/getErrorModal";
import parseSubmit from './getCreationUtils/parseSubmit';


const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser();

function handleModalVisible(setModalVisible, modalVisible) {
    setModalVisible(!modalVisible);
}

const GetCreationModal=forwardRef((props,ref) =>{
    const partidaVacia = {
        numJugadores: 2,
        conFlor: false,
        puntosMaximos: false,
        visibilidad: false, 
        
    };
   
  
    const [numJugadores, setNumJugadores] = useState(2);
    const [message, setMessage] = useState(null); 
    const [visible, setVisible] = useState(false);
    const [partidaVaciaParse, setPartidaVaciaParse] = useFetchState(
        [],
        '/api/v1/partida/0',
        jwt,
        setMessage,
        setVisible,
        0 
        );

        
    const [partidaParseada, setPartidaParseada] = useState(partidaVaciaParse)
    const[partida, setPartida] = useState(partidaVacia);
    
    const modal = getErrorModal(setVisible, visible, message);




    function handleChange(name){
        console.log(partida)
        if (name === 'puntosMaximos') {
            partida.puntosMaximos=!partida.puntosMaximos
        }
        
        if (name === 'visibilidad') {
            partida.visibilidad=!partida.visibilidad
        }

        if (name === 'conFlor') {
           partida.conFlor=!partida.conFlor
        }

        if (name === 'numJugadores2') {
            partida.numJugadores=2
            setNumJugadores(2);
         }

         if (name === 'numJugadores4') {
            partida.numJugadores=4
            setNumJugadores(4);
         }
         
         if (name === 'numJugadores6') {
            partida.numJugadores=6
            setNumJugadores(6);
         }
        
}


   function handleSubmit(event) {
    const  partidaParseada = parseSubmit(partida, partidaVaciaParse);
    
                                         
    console.log(partidaParseada)
    event.preventDefault();
    fetch("/api/v1/partida?userId="+user.id, {
      method:  "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(partidaParseada),
    })
      .then((response) => response.json())
      .then((json) => {
        if(json.statusCode === 500){
            alert("Error al crear la partida");
        }else{
            if (json.message) {
                setMessage(json.message);
                setVisible(true); 
              } else 
              window.location.href = "/partidas?partidaCode="+partidaParseada.codigo;
        }

      })
      .catch((message) => alert(message));
  }
  
    return (

        <div >
            <IoCloseCircle style={{ top:"4%", right:"5%", width: 30, height: 30, cursor: "pointer", position: "absolute", color: "rgb(123, 27, 0)"}} onClick={() => handleModalVisible(props.setCreationModalView, props.creationModalView)} />
            {modal}
            <div className= 'cuadro-creacion'>
                <div style={{marginTop:"20px"}}>
                    <Form onSubmit={handleSubmit}>
                        <h3> Ajuste de la partida: </h3>
                         <div className='contenedor'>
                            <Label> Numero de jugadores: </Label>
                                <div class="paste-button">               
                                    <a class="button2" style={{borderColor: "#ffa200"}}>
                                    {numJugadores ? `${numJugadores}▼` : '▼'}    
                                    </a> 
                                        <div class="dropdown-content">
                                            <a id="top" name="numJugadores2" onClick={()=>handleChange("numJugadores2")}>2</a>  
                                            <a id="middle" name="numJugadores4" onClick={() => handleChange("numJugadores4")}>4</a>
                                            <a id="bottom" name="numJugadores6" onClick={() => handleChange("numJugadores6")}>6</a>
                                    
                                         </div>
                                    </div>
                        </div> 

       
                        <div className='contenedor'>
                        <Label>
                             Puntos:
                            </Label>
                                <p style={{ marginRight:"10px",marginLeft:"170px"}}>15</p>
                                <div class="checkbox-wrapper-51">

                                    <input name= 'puntosMaximos' id="cbx-51" type="checkbox" onChange={() => handleChange("puntosMaximos")}/>
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

                                    <input  name= 'conFlor' id="cbx-52" type="checkbox" onChange={() => handleChange("conFlor")} />
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

                                    <input  name= 'visibilidad' id="cbx-53" type="checkbox" onChange={() => handleChange ('visibilidad')} />
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
                                <button className="botonCrear" onClick={() => handleSubmit}>Crear</button>
                        </div>
                    </Form>
                </div>
            </div>
        </div>
    )



}
)
export default GetCreationModal