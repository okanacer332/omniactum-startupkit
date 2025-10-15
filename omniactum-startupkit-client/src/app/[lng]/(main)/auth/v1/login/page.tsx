import Image from "next/image";
import React from 'react';
import { LoginForm } from "../../_components/login-form";
import { LanguageSwitcher } from "@/components/language-switcher";

import omniactumLogo from "@/../public/logos/omniactum-logo.png";

type PageProps = {
  params: Promise<{ lng: string }>;
};

export default function LoginV1({ params }: PageProps) {
  const { lng } = React.use(params);

  return (
    <div className="flex h-dvh flex-col lg:flex-row">
      <div className="hidden border-r bg-background lg:block lg:w-1/3">
        <div className="flex h-full flex-col items-center justify-center p-12 text-center">
          <div className="space-y-6">
            <Image
              src={omniactumLogo}
              alt="Omniactum Logo"
              width={250}
              quality={100}
              priority
            />
          </div>
        </div>
      </div>

      <div className="relative flex w-full flex-1 flex-col items-center justify-center bg-background p-8 lg:w-2/3">
        <div className="absolute top-4 right-4">
          <LanguageSwitcher />
        </div>

        <div className="flex w-full justify-center pt-8 pb-4 lg:hidden">
          <Image
            src={omniactumLogo}
            alt="Omniactum Logo"
            width={150}
            height={150}
            quality={100}
            priority
          />
        </div>
        
        <div className="my-auto w-full max-w-md">
          <LoginForm lng={lng} />
        </div>
      </div>
    </div>
  );
}