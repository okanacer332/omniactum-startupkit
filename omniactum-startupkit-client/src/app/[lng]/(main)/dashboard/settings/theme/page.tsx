"use client";

import { useEffect } from "react";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { toast } from "sonner";
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
  const { control, handleSubmit, reset, formState: { isSubmitting, isDirty } } = useForm<ThemeFormValues>({
    resolver: zodResolver(themeSchema),
    defaultValues: {
      dashboardBackground: "#f4f4f5",
      primaryButton: "#18181b",
      destructiveButton: "#ef4444",
      sidebarBackground: "#ffffff",
    },
  });

  useEffect(() => {
    // Sayfa yüklendiğinde mevcut ayarları çek
    apiFetchAuth("/api/settings/theme")
      .then(res => res.json())
      .then(data => {
        reset(data); // Formu gelen verilerle güncelle
      });
  }, [reset]);

  const onSubmit = async (data: ThemeFormValues) => {
    try {
      await apiFetchAuth("/api/settings/theme", {
        method: "PUT",
        body: JSON.stringify(data),
      });
      toast.success("Tema renkleri başarıyla güncellendi.");
      // Canlı önizleme için sayfayı yenilemeye zorlayabiliriz
      window.location.reload();
    } catch (error: any) {
      toast.error("Bir hata oluştu.", { description: error.message });
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Tema Renkleri</h1>
        <p className="text-muted-foreground">Sistem genelindeki ana renk paletini buradan yönetebilirsiniz.</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Renk Ayarları</CardTitle>
          <CardDescription>
            Değişiklikler kaydedildikten sonra tüm kullanıcılar için geçerli olacaktır.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-8">
            <ColorInput name="dashboardBackground" label="Dashboard Arka Plan Rengi" control={control} />
            <ColorInput name="primaryButton" label="Ana Buton Rengi (Kaydet, Giriş)" control={control} />
            <ColorInput name="destructiveButton" label="İptal/Sil Butonu Rengi" control={control} />
            <ColorInput name="sidebarBackground" label="Menü Arka Plan Rengi" control={control} />

            <Button type="submit" disabled={isSubmitting || !isDirty}>
              {isSubmitting ? "Kaydediliyor..." : "Değişiklikleri Kaydet"}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}

// Tekrarı önlemek için yardımcı bileşen
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