"use client";

import { Sidebar, SidebarContent, SidebarFooter } from "@/components/ui/sidebar";
import { sidebarItems } from "@/navigation/sidebar/sidebar-items";
import { NavMain } from "./nav-main";

interface AppSidebarProps extends React.ComponentProps<typeof Sidebar> {
  lng: string;
}

export function AppSidebar({ lng, ...props }: AppSidebarProps) {
  return (
    <Sidebar {...props}>
      <SidebarContent>
        <div className="p-2">
          {/* Tenant Switcher future location */}
        </div>
        <NavMain items={sidebarItems} lng={lng} />
      </SidebarContent>
    </Sidebar>
  );
}