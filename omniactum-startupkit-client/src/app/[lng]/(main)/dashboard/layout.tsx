// @ts-nocheck
// src/app/[lng]/(main)/dashboard/layout.tsx
import { ReactNode } from "react";
import { cookies } from "next/headers";
import { unstable_noStore as noStore } from "next/cache";

export const dynamic = "force-dynamic";

import AuthGuard from "./_components/auth-guard";
import { AuthProvider } from "./_components/auth-provider";
import { AppSidebar } from "./_components/sidebar/app-sidebar"; 
import { Separator } from "@/components/ui/separator";
import { SidebarInset, SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { cn } from "@/lib/utils";
import { getPreference } from "@/server/server-actions";
import {
  SIDEBAR_VARIANT_VALUES,
  SIDEBAR_COLLAPSIBLE_VALUES,
  CONTENT_LAYOUT_VALUES,
  type SidebarVariant,
  type SidebarCollapsible,
  type ContentLayout,
} from "@/types/preferences/layout";
import { AccountSwitcher } from "./_components/sidebar/account-switcher";
import { ThemeSwitcher } from "./_components/sidebar/theme-switcher";
import { LanguageSwitcher } from "@/components/language-switcher"; 
import { SidebarInitializer } from "./_components/sidebar-initializer"; 
// 1. ThemeApplicator IMPORT'U KALDIRILDI

// TİP TANIMI: Next.js'in beklediği parametre yapısını yansıtıyoruz
type LayoutParams = {
  params: { lng: string };
  children: ReactNode;
};

// Fonksiyonu Promise prop'unu alacak şekilde tanımlıyoruz
export default async function Layout({ children, params }: Readonly<LayoutParams>) {
  noStore();
  
  // lng'ye doğrudan erişiyoruz.
  const lng = params.lng; 

  const cookieStore = await cookies();

  const [sidebarVariant, sidebarCollapsible, contentLayout] = await Promise.all([
    getPreference<SidebarVariant>("sidebar_variant", SIDEBAR_VARIANT_VALUES, "inset"),
    getPreference<SidebarCollapsible>("sidebar_collapsible", SIDEBAR_COLLAPSIBLE_VALUES, "icon"),
    getPreference<ContentLayout>("content_layout", CONTENT_LAYOUT_VALUES, "centered"),
  ]);

  return (
    <SidebarProvider> 
      {/* 2. <ThemeApplicator /> SATIRI BURADAN KALDIRILDI */}
      <SidebarInitializer />
      <AuthGuard />
      <AppSidebar 
        key={lng} 
        variant={sidebarVariant} 
        collapsible={sidebarCollapsible} 
        lng={lng} 
      />
      <SidebarInset
        data-content-layout={contentLayout}
        className={cn(
          "data-[content-layout=centered]:!mx-auto data-[content-layout=centered]:max-w-screen-2xl",
          "max-[113rem]:peer-data-[variant=inset]:!mr-2 min-[101rem]:peer-data-[variant=inset]:peer-data-[state=collapsed]:!mr-auto",
        )}
      >
        <header className="flex h-12 shrink-0 items-center gap-2 border-b transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-12">
          <div className="flex w-full items-center justify-between px-4 lg:px-6">
            <div className="flex items-center gap-1 lg:gap-2">
              <SidebarTrigger className="-ml-1" />
            </div>
            <div className="flex items-center gap-2">
              <LanguageSwitcher />
              <ThemeSwitcher />
              <AccountSwitcher lng={lng} /> 
            </div>
          </div>
        </header>

        <AuthProvider>
          <div className="h-full p-4 md:p-6">{children}</div>
        </AuthProvider>

      </SidebarInset>
    </SidebarProvider>
  );
}