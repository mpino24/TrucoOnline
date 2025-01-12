import PropTypes from 'prop-types';
import MultiRangeSlider from './inputs/multiRangeSlider';
import FilePondPluginImageExifOrientation from 'filepond-plugin-image-exif-orientation';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import FilePondPluginFileEncode from 'filepond-plugin-file-encode';

import {
  useState,
  useEffect,
  useRef,
  forwardRef,
  useImperativeHandle
} from 'react';

import { FilePond, registerPlugin } from 'react-filepond';

const FormInput = forwardRef(
  (
    {
      tag,
      name,
      type,
      defaultValue,
      values,
      isRequired,
      numberOfColumns,
      validators,
      minValue,
      maxValue,
      onChange,
      disabled,
      // NEW PROP HERE:
      labelStyle
    },
    ref
  ) => {
    const [inputErrors, setInputErrors] = useState([]);
    const [files, setFiles] = useState([]);
    const [minInputValue, setMinInputValue] = useState(minValue);
    const [maxInputValue, setMaxInputValue] = useState(maxValue);

    const inputField = useRef(null);

    useImperativeHandle(ref, () => {
      return {
        setErrors: (errors) => {
          setInputErrors(errors);
        },
        value: inputField.current ? inputField.current.value : "",
        min: minInputValue,
        max: maxInputValue,
        files: files
      };
    });

    function handleFiles(fileItems) {
      setFiles(fileItems);
    }

    useEffect(() => {
      if (type !== "interval" && type !== "files") {
        // Attach an onChange listener to validate whenever the field changes
        inputField.current.addEventListener("change", () => {
          let errors = [];
          validators.forEach((validator) => {
            if (!validator.validate(inputField.current.value)) {
              errors.push(validator.message);
            }
          });
          setInputErrors(errors);
          if (onChange !== null) {
            onChange({ value: inputField.current.value });
          }
        });
      }
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    switch (type) {
      case "select":
        return (
          <div
            className={`class-form-group ${
              inputErrors.length > 0 ? "class-error-form" : ""
            }`}
            id={`${name}_form`}
            style={
              numberOfColumns > 1
                ? { paddingTop: "2%", width: `${100 / numberOfColumns - 3}%` }
                : { marginTop: "7.5%" }
            }
          >
            <select
              className="class-form-input"
              disabled={disabled}
              id={`${name}`}
              name={`${name}`}
              required={isRequired}
              defaultValue={defaultValue}
              ref={inputField}
            >
              {values &&
                values.map((option, index) => {
                  // "selected" is typically managed by defaultValue, so no need to do selected manually
                  return (
                    <option key={index} value={option}>
                      {option}
                    </option>
                  );
                })}
            </select>
            {/* Apply the style prop to the label here */}
            <label
              htmlFor={`${name}`}
              className="class-form-label"
              style={labelStyle}
            >
              {tag}:
            </label>
            {inputErrors.length > 0 &&
              inputErrors.map((error, index) => {
                return (
                  <span key={index} className="class-error-message">
                    {error}
                  </span>
                );
              })}
          </div>
        );

      case "textarea":
        return (
          <div
            className={`class-form-group ${
              inputErrors.length > 0 ? "class-error-form" : ""
            }`}
            id={`${name}_form`}
            style={{ width: "100%" }}
          >
            <textarea
              className="class-form-input"
              disabled={disabled}
              type={type}
              id={`${name}`}
              name={`${name}`}
              placeholder=" "
              defaultValue={defaultValue ? defaultValue : ""}
              required={isRequired}
              ref={inputField}
            />
            <label
              htmlFor={`${name}`}
              className="class-form-label"
              style={labelStyle}
            >
              {tag}:
            </label>
            {inputErrors.length > 0 &&
              inputErrors.map((error, index) => {
                return (
                  <span key={index} className="class-error-message">
                    {error}
                  </span>
                );
              })}
          </div>
        );

      case "interval":
        return (
          <div
            className={`class-form-group interval-group d-flex justify-content-evenly ${
              inputErrors.length > 0 ? "class-error-form" : ""
            }`}
            id={`${name}_form`}
            style={
              numberOfColumns > 1
                ? { width: `${100 / numberOfColumns - 3}%` }
                : {}
            }
          >
            <label
              htmlFor={`${name}`}
              className="class-form-label"
              style={labelStyle}
            >
              {tag}:
            </label>
            <MultiRangeSlider
              min={minValue}
              max={maxValue}
              onChange={({ min, max }) => {
                setMinInputValue(min);
                setMaxInputValue(max);
              }}
            />
          </div>
        );

      case "files":
        registerPlugin(
          FilePondPluginImageExifOrientation,
          FilePondPluginImagePreview,
          FilePondPluginFileEncode
        );

        return (
          <div
            className={`class-form-group files-group`}
            id={`${name}_form`}
            style={{ paddingTop: "2%", width: "100%" }}
          >
            <label
              htmlFor={`${name}`}
              className="class-form-label"
              style={labelStyle}
            >
              {tag}:
            </label>
            <FilePond
              files={files}
              onupdatefiles={handleFiles}
              allowMultiple={true}
              allowReorder={true}
              maxFiles={10}
              name={name}
              labelIdle='Arrastra tus archivos o <span class="filepond--label-action">Selecciona</span>'
              credits={false}
            />
          </div>
        );

      case "date":
        return (
          <div
            className={`class-form-group ${
              inputErrors.length > 0 ? "class-error-form" : ""
            }`}
            id={`${name}_form`}
            style={
              numberOfColumns > 1
                ? { paddingTop: "2%", width: `${100 / numberOfColumns - 3}%` }
                : {}
            }
          >
            <input
              className="class-form-input"
              disabled={disabled}
              type="date"
              id={`${name}`}
              name={`${name}`}
              required={isRequired}
              defaultValue={defaultValue}
              ref={inputField}
            />
            <label
              htmlFor={`${name}`}
              className="class-form-label"
              style={labelStyle}
            >
              {tag}:
            </label>
            {inputErrors.length > 0 &&
              inputErrors.map((error, index) => {
                return (
                  <span key={index} className="class-error-message">
                    {error}
                  </span>
                );
              })}
          </div>
        );

      default:
        // typical text/password/email/number, etc.
        return (
          <div
            className={`class-form-group ${
              inputErrors.length > 0 ? "class-error-form" : ""
            }`}
            id={`${name}_form`}
            style={
              numberOfColumns > 1
                ? { width: `${100 / numberOfColumns - 3}%` }
                : {}
            }
          >
            <input
              className="class-form-input"
              disabled={disabled}
              type={type}
              id={`${name}`}
              name={`${name}`}
              placeholder=" "
              defaultValue={defaultValue ? defaultValue : ""}
              required={isRequired}
              ref={inputField}
            />
            {/* APPLY the style to the label */}
            <label
              htmlFor={`${name}`}
              className="class-form-label"
              style={labelStyle}
            >
              {tag}:
            </label>
            {inputErrors.length > 0 &&
              inputErrors.map((error, index) => {
                return (
                  <span key={index} className="class-error-message">
                    {error}
                  </span>
                );
              })}
          </div>
        );
    }
  }
);

FormInput.propTypes = {
  tag: PropTypes.string,
  name: PropTypes.string,
  type: PropTypes.oneOf([
    "text",
    "password",
    "email",
    "number",
    "select",
    "textarea",
    "interval",
    "files",
    "date",
    "flatter-tags",
    "datetime-local"
  ]),
  values: PropTypes.array,
  defaultValue: PropTypes.string,
  isRequired: PropTypes.bool,
  minValue: PropTypes.number,
  maxValue: PropTypes.number,
  numberOfColumns: PropTypes.number,
  validators: PropTypes.array,
  formValues: PropTypes.object,
  setFormValues: PropTypes.func,
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  // NEW:
  labelStyle: PropTypes.object
};

FormInput.defaultProps = {
  tag: "default",
  name: "default",
  type: "text",
  defaultValue: "",
  numberOfColumns: 1,
  values: [],
  isRequired: false,
  minValue: 0,
  maxValue: 100,
  validators: [],
  formValues: {},
  setFormValues: () => {},
  onChange: null,
  disabled: false,
  // NEW:
  labelStyle: {}
};

export default FormInput;
