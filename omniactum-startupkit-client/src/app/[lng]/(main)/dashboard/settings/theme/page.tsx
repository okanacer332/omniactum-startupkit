"use client";

// 1. GEREKLİ HOOK'LARI IMPORT ET
import { useEffect, useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { toast } from "sonner";
import { useParams } from "next/navigation";
import { useTranslation } from "@/lib/i18n-client";

import { apiFetchAuth } from "@/lib/api-auth";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";

// DTO ile aynı yapıda Zod şeması
const themeSchema = z.object({
  dashboardBackground: z.string(),
  primaryButton: z.string(),
  destructiveButton: z.string(),
  sidebarBackground: z.string(),
});

type ThemeFormValues = z.infer<typeof themeSchema>;

export default function ThemeSettingsPage() {
  // 2. YÜKLEME VE ÇEVİRİ HOOK'LARINI EKLE
  const [isLoading, setIsLoading] = useState(true);
  const { lng } = useParams() as { lng: string };
  const { t, ready } = useTranslation(lng, 'common');

  const { control, handleSubmit, reset, formState: { isSubmitting, isDirty } } = useForm<ThemeFormValues>({
    resolver: zodResolver(themeSchema),
    defaultValues: {
      dashboardBackground: "#f4f4f5",
      primaryButton: "#18181b",
      destructiveButton: "#ef4444",
      sidebarBackground: "#ffffff",
    },
  });

  // 3. useEffect'i GÜNCELLE (sadece 'ready' olunca çalışsın)
  useEffect(() => {
    if (!ready) return;

    setIsLoading(true);
    apiFetchAuth("/api/settings/theme")
      .then(res => res.json())
      .then(data => {
        reset(data);
      })
      .catch(() => {
        toast.error(t("themeSettings.toast.errorTitle"), { description: t("themeSettings.toast.errorDescription") });
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [reset, ready, t]);

  const onSubmit = async (data: ThemeFormValues) => {
    try {
      await apiFetchAuth("/api/settings/theme", {
        method: "PUT",
        body: JSON.stringify(data),
      });
      // 4. TOAST MESAJLARINI ÇEVİR
      toast.success(t("themeSettings.toast.success"));
      window.location.reload();
    } catch (error: any) {
      toast.error(t("themeSettings.toast.errorTitle"), { description: t("themeSettings.toast.errorDescription") });
    }
  };

  // 5. YÜKLENİYORSA VEYA ÇEVİRİ HAZIR DEĞİLSE SKELETON GÖSTER
  if (isLoading || !ready) {
    return (
        <div className="space-y-6">
            <div>
                <Skeleton className="h-8 w-1/3" />
                <Skeleton className="mt-2 h-5 w-2/3" />
            </div>
            <Card>
                <CardHeader>
                    <Skeleton className="h-7 w-1/4" />
                    <Skeleton className="mt-2 h-5 w-full" />
                </CardHeader>
                <CardContent className="space-y-8">
                    <Skeleton className="h-10 w-full" />
                    <Skeleton className="h-10 w-full" />
                    <Skeleton className="h-10 w-full" />
                    <Skeleton className="h-10 w-full" />
                    <Skeleton className="h-9 w-32" />
                </CardContent>
            </Card>
        </div>
    );
  }

  // 6. TÜM METİNLERİ t() FONKSİYONU İLE DEĞİŞTİR
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">{t("themeSettings.page.title")}</h1>
        <p className="text-muted-foreground">{t("themeSettings.page.description")}</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>{t("themeSettings.card.title")}</CardTitle>
          <CardDescription>
            {t("themeSettings.card.description")}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-8">
            <ColorInput name="dashboardBackground" label={t("themeSettings.form.dashboardBackground")} control={control} />
            <ColorInput name="primaryButton" label={t("themeSettings.form.primaryButton")} control={control} />
            <ColorInput name="destructiveButton" label={t("themeSettings.form.destructiveButton")} control={control} />
            <ColorInput name="sidebarBackground" label={t("themeSettings.form.sidebarBackground")} control={control} />

            <Button type="submit" disabled={isSubmitting || !isDirty}>
              {isSubmitting ? t("themeSettings.form.savingButton") : t("themeSettings.form.saveButton")}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}

// Bu yardımcı bileşen aynı kalabilir.
const ColorInput = ({ name, label, control }: { name: keyof ThemeFormValues, label: string, control: any }) => (
  <Controller
    name={name}
    control={control}
    render={({ field }) => (
      <div className="flex items-center gap-4">
        <Label htmlFor={name} className="w-1/3">{label}</Label>
        <div className="relative flex items-center gap-2">
            <Input
                id={name}
                type="color"
                className="w-12 h-10 p-1"
                {...field}
            />
             <Input
                type="text"
                className="w-28"
                value={field.value}
                onChange={field.onChange}
            />
        </div>
      </div>
    )}
  />
);