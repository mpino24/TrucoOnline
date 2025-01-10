import { useState, forwardRef } from "react";
import { Form, Label, Input } from "reactstrap";
import tokenService from "../../services/token.service";
const jwt = tokenService.getLocalAccessToken();

function handleModalVisible(setModalVisible, modalVisible) {
  setModalVisible(!modalVisible);
}

const GetCreationLogroModal = forwardRef((props, ref) => {
  const { setCreationLogroModal, creationLogroModal } = props;
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    valor: 10,
    metrica: "VICTORIAS",
    descripcion: "",
    imagencita: "http://localhost:8080/resources/images/trofeos/trofeo1.jpg",
    oculto: false,
  });

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
    console.log(formData);

    fetch("/api/v1/logros", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then((response) => response.json())
      .then((json) => {
        handleModalVisible(setCreationLogroModal, creationLogroModal);
        props.setActualizarLista(props.actualizarLista + 1);
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
        <div>
          <Label style={estiloLetras}>Nombre: </Label>
          <Input
            type="text"
            required
            name="name"
            placeholder="Ponele un nombre a tu nuevo logro"
            id="name"
            value={formData.name}
            onChange={handleChange}
            className="custom-input"
            minLength="3"
            maxLength="50"
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
          <div className="switch-container">
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
            placeholder="Describí brevemente el logro nuevo"
            name="descripcion"
            id="descripcion"
            value={formData.descripcion}
            onChange={handleChange}
            className="custom-input"
            pattern=".*\S.*"
            title="La descripción debe tener al menos un carácter que no sea un espacio"
          />
        </div>
        <div className="custom-form-input">
          <Label for="photo" className="custom-form-input-label" style={estiloLetras}>
            Foto
          </Label>
          {formData.imagencita && (
            <img
              src={formData.imagencita}
              alt="Foto del perfil"
              style={{
                width: "100px",
                height: "100px",
                borderRadius: "50%",
              }}
              onError={(e) => (e.target.style.display = "none")}
            />
          )}
          <Label for="photo" className="custom-form-input-label" style={estiloLetras}>
            Foto
          </Label>
          <Input
            type="text"
            name="photo"
            id="photo"
            value={formData.imagencita}
            onChange={handleChange}
            className="custom-input"
            placeholder="Poné el link de la foto que quieras usar"
          />
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
            onClick={() => handleModalVisible(setCreationLogroModal, creationLogroModal)}
            className="auth-button">
            Cancelar
          </button>
        </div>
      </Form>
    </>
  );
});

export default GetCreationLogroModal;
