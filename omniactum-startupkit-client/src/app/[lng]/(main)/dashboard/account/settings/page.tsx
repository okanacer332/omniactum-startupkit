// aj-client/src/app/[lng]/(main)/dashboard/account/settings/page.tsx
"use client";

import { Skeleton } from "@/components/ui/skeleton";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ProfileForm } from "./profile-form";
import { SecurityForm } from "./security-form";
import { useAuthStore } from "@/stores/auth-store";
import { useTranslation } from "@/lib/i18n-client";
import { useParams } from "next/navigation";

export default function AccountSettingsPage() {
  const { lng } = useParams() as { lng: string };
  const { t, ready } = useTranslation(lng, 'common');
  const { user, isLoading, fetchUser } = useAuthStore();

  if (isLoading || !ready) {
    return (
      <div className="space-y-4">
        <Skeleton className="h-10 w-1/3" />
        <Skeleton className="h-64 w-full" />
      </div>
    );
  }

  if (!user) {
    return <p>Kullanıcı bilgileri yüklenemedi.</p>;
  }

   return (
    <div className="flex flex-col gap-6">
       <div>
        <h1 className="text-2xl font-bold tracking-tight">{t('account.settings.title')}</h1>
        <p className="text-muted-foreground">{t('account.settings.description')}</p>
      </div>
      <Tabs defaultValue="profile" className="w-full">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="profile">{t('account.settings.tab.profile')}</TabsTrigger>
          <TabsTrigger value="security">{t('account.settings.tab.security')}</TabsTrigger>
        </TabsList>
        <TabsContent value="profile" className="pt-4">
          <ProfileForm user={user} onSuccess={fetchUser} lng={lng} />
        </TabsContent>
        <TabsContent value="security" className="pt-4">
          <SecurityForm lng={lng} />
        </TabsContent>
      </Tabs>
    </div>
  );
}