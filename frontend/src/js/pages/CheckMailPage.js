import {useNavigate} from "react-router-dom";

function CheckMailPage() {

    return (<div><div className="generalBlock" id="generalBlock" style={{
            width: "70%",
            marginLeft: "auto",
            marginRight: "auto",
            borderRadius: "15px",
            backgroundColor: "#ffffff",
            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
            verticalAlign: "top",
            fontFamily: "inherit",
            minHeight: "250px",
        }}>
            <div className="logo">
                <img
                    className="imgLogo"
                    src="media/logoWithoutIcon.png"
                    alt="Notification System"
                    style={{
                        marginTop: "2rem",
                        width: "50%",
                        display: "block",
                        marginLeft: "auto",
                        marginRight: "auto",
                        background: "#ffffff",
                        borderRadius: "5px",
                        padding: "2%",
                    }}
                />
            </div>
            <div className="message" style={{
                margin: "0% 100px 3rem",
            }}>
                <p>
                    Пожалуйста, подтвердите почту. Для этого необходимо проверить
                    указанную почту и перейти по ссылке в письме от Notification System.
                </p>
                <p>
                    Please confirm your email. To do this, you need to check the
                    specified mail and follow the link in the letter from Notification
                    System.
                </p>
            </div>
        </div>
    </div>
    );
}

export default CheckMailPage;