import {Col, Dropdown, MenuProps, Row, Space, Typography} from "antd";
import {DownOutlined, LogoutOutlined, NotificationOutlined, UserOutlined,} from "@ant-design/icons";

const Header = () => {

  const items: MenuProps["items"] = [
    {
      key: "logout",
      icon: <LogoutOutlined />,
      label: "Logout",
    },
  ];

  return (
    <Row
      justify="space-between"
      style={{
        height: 42,
        backgroundColor: "#fff",
        padding: "5px 20px",
        borderBottom: "1px solid #cecece",
      }}
    >

      <Col />

      <Col style={{color: "#666"}}>
        <Space size={10}>
          <NotificationOutlined style={{fontSize: "14px"}} />
          <Dropdown.Button menu={{items: items}} icon={<DownOutlined />}
                           overlayClassName="shadow-md"
                           overlayStyle={{width: 200}}
                           placement="bottomRight"
                           type="text">
            <UserOutlined
              style={{
                height: "16px",
                width: "16px",
                fontSize: "14px",
                backgroundColor: "#fff",
                border: "1px solid #666",
                borderRadius: "50%",
              }}
            />
            <span style={{fontSize: "14px", color: "#666"}}>Hacker#00</span>
          </Dropdown.Button>
        </Space>
      </Col>
    </Row>
  );
};

export default Header;
