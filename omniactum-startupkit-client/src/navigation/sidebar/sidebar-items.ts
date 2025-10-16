// src/navigation/sidebar/sidebar-items.ts
import {
  LayoutDashboard,
  Settings,
  UserCog,
  ShieldCheck,
  FileClock,
  Palette, // Palette ikonunu ekleyin
  type LucideIcon,
} from "lucide-react";

export interface NavSubItem {
  title: string;
  url: string;
  icon?: LucideIcon;
  permission?: string;
}

export interface NavMainItem {
  title: string;
  url: string;
  icon?: LucideIcon;
  subItems?: NavSubItem[];
  permission?: string; // Ana menü öğesi için de yetki tanımı
}

export interface NavGroup {
  id: number;
  label?: string;
  items: NavMainItem[];
}

export const sidebarItems: NavGroup[] = [
  {
    id: 1,
    label: "sidebar.managementPanel.label",
    items: [
      {
        title: "sidebar.managementPanel.home",
        url: "/dashboard/default",
        icon: LayoutDashboard,
      },
    ],
  },
  {
    id: 2,
    label: "sidebar.modules.label",
    items: [
      {
        title: "sidebar.modules.systemManagement",
        url: "#",
        icon: Settings,
        subItems: [
          { title: "sidebar.modules.userManagement", url: "/dashboard/iam/users", icon: UserCog, permission: "PAGE_USERS:READ" },
          { title: "sidebar.modules.roleManagement", url: "/dashboard/iam/roles", icon: ShieldCheck, permission: "PAGE_ROLES:READ" },
          { title: "sidebar.modules.auditLogs", url: "/dashboard/audit/logs", icon: FileClock, permission: "PAGE_LOGS:READ" },
        ],
      },
    ],
  },
  {
    id: 3,
    label: "sidebar.modules.siteSettings",
    items: [
      {
        title: "sidebar.modules.themeColors",
        url: "/dashboard/settings/theme",
        icon: Palette,
        permission: "PAGE_THEME_SETTINGS:WRITE", // Sadece yazma yetkisi olan görsün
        // subItems: [] propertysini kaldırıyoruz çünkü bu bir direkt link
      },
    ],
  },
];
