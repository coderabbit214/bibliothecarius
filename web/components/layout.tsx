import {Layout} from "antd";
import React from "react";
import Sidebar from "./sidebar";
import Header from "./header";

const {Content, Footer} = Layout;


type Props = {
  children: React.ReactNode
}

const LayoutWrapper = ({children}: Props) => {

  return (
    <Layout style={{minHeight: '100vh'}}>
      <Sidebar />
      <Layout className="bg-white">
        <Header />
        <Content style={{margin: '0 16px'}}>
          <div className="bg-white" style={{padding: 24, minHeight: 360}}>
            {children}
          </div>
        </Content>
        <Footer style={{textAlign: 'center'}}>Convect AI ©2022</Footer>
      </Layout>
    </Layout>
  );
};

export default LayoutWrapper;
