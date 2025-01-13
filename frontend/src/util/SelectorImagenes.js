import React from "react";
import { Modal, ModalBody } from "reactstrap";

const SelectorImagenes = ({ imagenes, isOpen, toggle, onSelect , tipo}) => {

  
  function completarURL(nombre) {
    let url = "http://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/"
    if(tipo==="trofeo"){
      url += "trofeos/"
    }else{
      url += "perfiles/"
    }
    return url +nombre
  }

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
    >
      <ModalBody
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fill, minmax(100px, 1fr))",
          gap: "10px",
          padding: "20px",
        }}
      >
        {imagenes.map((nombre) => (
          <img
            key={nombre}
            src={completarURL(nombre)}
            alt={nombre}
            onClick={() => onSelect(nombre)}
            style={{
              width: "100%",
              height: "100px",
              cursor: "pointer",
              objectFit: "cover",
              borderRadius: "10px",
              border: "2px solid transparent",
            }}
          />
        ))}
      </ModalBody>
    </Modal>
  );
};

export default SelectorImagenes;
