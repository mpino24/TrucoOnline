import { useState, useEffect, forwardRef } from "react";
import { Form, Label, Input } from "reactstrap";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import SelectorImagenes from "../../util/SelectorImagenes";
const jwt = tokenService.getLocalAccessToken();



const EditLogroModal = forwardRef((props, ref) => {
  const { setEditLogroModal, editLogroModal, logro, setActualizarLista, actualizarLista, setLogroSeleccionado } = props;
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    valor: 10,
    metrica: "PARTIDAS_A_2",
    descripcion: "",
    imagencita: "https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/trofeos/trofeo1.jpg",
    oculto: false,
  });
  const [imageModalOpen, setImageModalOpen] = useState(false);

  const [imagenesDisponibles, setImagenesDisponibles] = useFetchState([], "https://prod.liveshare.vsengsaas.visualstudio.com/join?28A62B20EB0907180FC1568D483BD36FB36D/api/v1/fotos/trofeos", jwt, setMessage, setVisible);

  const handleImageSelect = (imageName) => {
    setFormData({
      ...formData,
      imagencita: `https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/trofeos/${imageName}`,
    });
    setImageModalOpen(false);
  };

  function handleModalVisible(setModalVisible, modalVisible) {
    setLogroSeleccionado(null)
    setModalVisible(!modalVisible);
  }
  useEffect(() => {
    if (logro) {
      setFormData({
        name: logro.name,
        valor: logro.valor,
        metrica: logro.metrica,
        descripcion: logro.descripcion,
        imagencita: logro.imagencita,
        oculto: logro.oculto,
      });
    }
  }, [logro]);

  const metricaOptions = [
    "PARTIDAS_JUGADAS",
    "TIEMPO_JUGADO",
    "VICTORIAS",
    "DERROTAS",
    "PARTIDAS_A_2",
    "PARTIDAS_A_4",
    "PARTIDAS_A_6",
    "NUMERO_FLORES",
    "NUMERO_ENGANOS",
    "QUIEROS",
    "NO_QUIEROS",
    "PARTIDAS_CON_FLOR",
    "ATRAPADO"
  ];

  const handleChange = (e) => {
    const target = e.target;
    const type = target.type;
    const checked = target.checked;
    const value = target.value;
    const name = target.name;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    fetch(`https://prod.liveshare.vsengsaas.visualstudio.com/join?28A62B20EB0907180FC1568D483BD36FB36D/api/v1/logros/${logro.id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then((response) => response.json())
      .then((json) => {
        console.log("devuelvo algo")
        handleModalVisible(setEditLogroModal, editLogroModal);
        setActualizarLista(actualizarLista + 1);
      })
      .catch((message) => alert(message));
  };

  const estiloLetras = {
    color: "orange",
    textShadow: "1px 1px 2px black"
  }

  return (
    <>
      <Form onSubmit={handleSubmit} style={{
        backgroundColor: 'rgb(255, 255, 255, 0.3)',
        color: 'white',
        padding: '20px',
        borderRadius: '10px'
      }}>
        <div >
          <Label style={estiloLetras}>Nombre: </Label>
          <Input
            type="text"
            required
            name="name"
            id="name"
            value={formData.name}
            onChange={handleChange}
            className="custom-input"
            minLength="3"
            maxLength="50"
            placeholder="Ponele un nombre nuevo a tu logro"

          />
        </div>
        <div>
          <Label style={estiloLetras}>Valor: </Label>
          <Input
            type="number"
            required
            name="valor"
            id="valor"
            value={formData.valor}
            onChange={handleChange}
            className="custom-input"
            min="0"
          />
        </div>
        <div>
          <Label style={estiloLetras}>Metrica: </Label>
          <div className="switch-container" >
            <Input
              type="select"
              name="metrica"
              id="metrica"
              value={formData.metrica}
              onChange={handleChange}
              className="custom-switch"
              style={{ backgroundColor: "transparent" }}
            >
              {metricaOptions.map((metrica) => (
                <option key={metrica} value={metrica}>
                  {metrica.replaceAll("_", " ")}
                </option>
              ))}
            </Input>
          </div>
        </div>
        <div>
          <Label style={estiloLetras}>Descripcion: </Label>
          <Input
            type="text"
            required
            name="descripcion"
            placeholder="Describi brevemente el nuevo logro"
            id="descripcion"
            value={formData.descripcion}
            onChange={handleChange}
            className="custom-input"
            pattern=".*\S.*"
            title="La descripción debe tener al menos un carácter que no sea un espacio"
          />
        </div>
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <Label style={estiloLetras}>Foto:  </Label>
          {formData.imagencita && (
            <img
              src={formData.imagencita}
              alt="Seleccionada"
              style={{
                width: "150px",
                height: "150px",
                borderRadius: "30%",
                marginBottom: "10px",
                objectFit: "cover",
              }}
            />
          )}
          <button
            type="button"
            onClick={() => setImageModalOpen(true)}
            className="auth-button"
            style={{
              marginBottom: "10px",
            }}
          >
            Seleccionar Imagen
          </button>
        </div>
        <div>
          <Label style={estiloLetras}>¿Oculto? </Label>
          <Input
            type="checkbox"
            name="oculto"
            id="oculto"
            checked={formData.oculto}
            onChange={handleChange}
            className="custom-input"
          />
        </div>

        <div className="custom-button-row">
          <button type="submit" className="auth-button">
            Guardar
          </button>
          <button
            type="button"
            onClick={() => handleModalVisible(setEditLogroModal, editLogroModal)}
            className="auth-button"
          >
            Cancelar
          </button>
        </div>
      </Form>
      <SelectorImagenes
        imagenes={imagenesDisponibles}
        isOpen={imageModalOpen}
        toggle={() => setImageModalOpen(!imageModalOpen)}
        onSelect={handleImageSelect}
        tipo={"trofeo"}
      />
    </>
  );
});

export default EditLogroModal;
