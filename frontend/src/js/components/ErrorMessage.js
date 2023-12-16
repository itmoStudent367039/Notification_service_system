function ErrorMessage({message}){
    return (
        <div>
            <div className="app">
                <div>
                    <i
                        className="fa fa-exclamation-triangle"
                        aria-hidden="true"
                        style={{color: "red", fontSize: "13px"}}
                    ></i>
                    <div
                        style={{
                            paddingLeft: "2rem",
                            color: "red",
                            fontSize: "small",
                            paddingTop: "0.3rem",
                        }}
                    >
                        {message}
                    </div>
                </div>
            </div>
        </div>
    );
}
export default ErrorMessage;