import {Layout, Menu, ModalProps} from "antd";
import {
  BlockOutlined,
  FolderOpenOutlined, MenuFoldOutlined,
  MenuUnfoldOutlined,
  PieChartOutlined,
  SettingOutlined,
} from "@ant-design/icons";
import React, {useEffect, useState} from "react";
import {MenuItemType} from "antd/es/menu/hooks/useItems";

import {useRouter} from "next/router";

const {Sider} = Layout;

type UrlMenuItem = MenuItemType & {
  url: string
}

function getItem(
  label: React.ReactNode,
  key: React.Key,
  icon?: React.ReactNode,
  url?: string,
  children?: UrlMenuItem[],
): UrlMenuItem {
  return {
    key,
    icon,
    children,
    label,
    url
  } as UrlMenuItem;
}

const items: UrlMenuItem[] = [
  getItem('Dashboard', 'dashboard', <PieChartOutlined />, '/'),
  getItem('Dataset', 'dataset', <FolderOpenOutlined />, '/dataset'),
  getItem('Scene', 'scene', <BlockOutlined />, '/scene'),
];

// make a map of the items witch key is key, value is item
const itemsMap = items.reduce((acc, item) => {
  item?.key && (acc[item.key] = item?.url);
  return acc;
}, {} as Record<React.Key, string>);

export interface SidebarProps<ValueType = any> extends ModalProps {
  onWidthChange?: (width: number) => void;
}

const Sidebar: React.FC<SidebarProps> = ({onWidthChange}: SidebarProps) => {
  const router = useRouter();
  const [collapsed, setCollapsed] = useState(false);

  useEffect(() => {
    if (collapsed) {
      onWidthChange ? onWidthChange(80) : null;
    } else {
      onWidthChange ? onWidthChange(200) : null;
    }
  }, [collapsed]);

  // onClick
  const handleRoute = (key: string) => {
    router.push(itemsMap[key]);
  };

  return (
    <Sider
      collapsible
      style={{
        overflow: "auto",
        height: "100vh",
        position: "sticky",
        top: 0,
        left: 0,
      }}
      collapsed={collapsed}
      onCollapse={(value) => setCollapsed(value)}
      trigger={
        <div style={{width: 80}}>
          {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
        </div>
      }
    >
      <div
        className="bg-contain bg-no-repeat bg-center bg-[url('/logo-white.png')]"
        style={{margin: "16px", height: "32px"}}
      />
      <Menu
        theme="dark"
        selectedKeys={[router.pathname.split("/")[1] || "dashboard"]}
        defaultSelectedKeys={["dashboard"]}
        defaultOpenKeys={items.map((item) => String(item.key))}
        mode="inline"
        items={items}
        onClick={({key}) => handleRoute(key)}
      />
    </Sider>
  );
};

export default Sidebar;
