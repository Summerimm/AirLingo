import styled from "@emotion/styled";
import PropTypes from "prop-types";

const radiusObj = {
    small: "10px",
    big: "20px",
};

function TextArea({ placeholder, radius, width, height, maxLength, value, onChange }) {
    return (
        <TextAreaWrapper
            placeholder={placeholder}
            radius={radius}
            width={width}
            height={height}
            maxLength={maxLength}
            value={value}
            onChange={onChange}
        />
    );
}

TextArea.propTypes = {
    placeholder: PropTypes.string,
    radius: PropTypes.oneOf(["small", "big"]),
    width: PropTypes.string,
    height: PropTypes.string,
    maxLength: PropTypes.number,
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
};

TextArea.defaultProps = {
    placeholder: "",
    radius: "small",
    width: "400px",
    height: "350px",
    maxLength: 500,
};

const TextAreaWrapper = styled.textarea`
    box-sizing: border-box;
    border-radius: ${({ radius }) => radiusObj[radius]};
    ${({ width, height }) => `
        width: ${width};
        height: ${height};
    `};
    border: 1px solid ${({ theme }) => theme.colors.primary4};
    background-color: ${({ theme }) => theme.colors.primary1};
    outline: none;
    padding: 10px 20px;
    color: ${({ theme }) => theme.colors.secondary3};
    resize: none;

    &::placeholder {
        color: ${({ theme }) => theme.colors.primary4};
    }
    &:hover {
        cursor: pointer;
    }
`;

export default TextArea;